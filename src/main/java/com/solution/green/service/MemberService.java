package com.solution.green.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.NonNull;
import com.solution.green.dto.MemberDto;
import com.solution.green.entity.Member;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.solution.green.code.DatabaseName.USERS;
import static com.solution.green.code.GreenErrorCode.*;


@Service
@RequiredArgsConstructor
public class MemberService {
    public static final String COLLECTION_NAME = USERS.getDescription(); // database name
    public static Firestore firestore = FirestoreClient.getFirestore();


    private final MemberRepository memberRepository;

    @Transactional
    public void testing() {
        memberRepository.save(Member.builder()
                                .name("name")
                                .email("email@gmail.com")
                                .password("password")
                                .build());
    }









    public String createMember(MemberDto.Request request) throws Exception{
        DocumentReference documentReference =
                getSpecificDocumentReference(request.getMemberId());
        if (validateMemberExists(documentReference))
            throw new GreenException(ALREADY_REGISTERED);
        else return documentReference.set(request)
                .get().getUpdateTime().toString();
    }
    private Boolean validateMemberExists(@NonNull DocumentReference documentReference) throws Exception {
        if (documentReference.get().get().exists()) return true;
        else return false;
    }
    public List<MemberDto.Response> getAllMembers() throws Exception{
        List<MemberDto.Response> responseList = new ArrayList<>();
        List<QueryDocumentSnapshot> documents =
                firestore.collection(COLLECTION_NAME).get().get().getDocuments();
        for (QueryDocumentSnapshot document : documents)
            responseList.add(document.toObject(MemberDto.Response.class));
        return responseList;
    }
    public MemberDto.Response getMemberDetail(String memberId) throws Exception{
        DocumentReference documentReference = getSpecificDocumentReference(memberId);
        if (!validateMemberExists(documentReference))
            throw new GreenException(NO_MEMBER);
        else return documentReference.get().get().toObject(MemberDto.Response.class);
    }
    public String editMember(String memberId, MemberDto.Request editRequest)
            throws Exception {
        DocumentReference documentReference = getSpecificDocumentReference(memberId);
        // TODO 어떤 내용을 editable 하게 설정할 건지 추후 논의 (반드시 id는 변경 불가하도록)
        if (!validateMemberExists(documentReference))
            throw new GreenException(NO_MEMBER);
        else return documentReference.set(editRequest)
                .get().getUpdateTime().toString();
    }

    public String deleteMember(String memberId) {
        getSpecificDocumentReference(memberId).delete();
        return "Document id: " + memberId + " delete";
    }

    private static DocumentReference getSpecificDocumentReference(String memberId) {
        return firestore.collection(COLLECTION_NAME).document(memberId);
    }

    public MemberDto.Response login(MemberDto.login loginMember) throws Exception {
        MemberDto.Response detail = getMemberDetail(loginMember.getMemberId());
        if (loginMember.getPassword().equals(detail.getPassword()))
            return detail;
        else throw new GreenException(WRONG_PASSWORD);
    }
}