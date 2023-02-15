package com.solution.green.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.NonNull;
import com.solution.green.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.solution.green.code.DatabaseName.USERS;


@Service
public class MemberService {
    public static final String COLLECTION_NAME = USERS.getDescription(); // database name
    public static Firestore firestore = FirestoreClient.getFirestore();

    public String createMember(MemberDto.Request request) throws Exception{
        DocumentReference documentReference =
                getSpecificDocumentReference(request.getMemberId());
        if (validateMemberExists(documentReference))
            throw new Exception(); // TODO - exception: already exist
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
            throw new Exception(); // TODO - exception: no user
        else return documentReference.get().get().toObject(MemberDto.Response.class);
    }
    public String editMember(String memberId, MemberDto.Request editRequest)
            throws Exception {
        DocumentReference documentReference = getSpecificDocumentReference(memberId);
        // TODO 어떤 내용을 editable 하게 설정할 건지 추후 논의 (반드시 id는 변경 불가하도록)
        if (!validateMemberExists(documentReference))
            throw new Exception(); // TODO - exception: no user
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
        else throw new Exception(); // TODO exception handling - wrong password
    }
}