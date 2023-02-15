package com.solution.green.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.NonNull;
import com.solution.green.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MemberService {
    public static final String COLLECTION_NAME = "users"; // database name
    // TODO database name -> code
    public static Firestore firestore = FirestoreClient.getFirestore();

    public String createMember(MemberDto.Request request) throws Exception{
        DocumentReference documentReference =
                validateCreateMemberRequest(request.getMemberId());
        ApiFuture<WriteResult> apiFuture = documentReference.set(request);
        return apiFuture.get().getUpdateTime().toString();
    }
    private DocumentReference validateCreateMemberRequest(@NonNull String memberId) throws Exception {
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(memberId);
        DocumentSnapshot documentSnapshot = documentReference.get().get();
        if (documentSnapshot.exists())
            throw new Exception(); // TODO implement - duplicate memberId
        else
            return documentReference;
    }
    private DocumentReference validateEditMemberRequest(@NonNull String memberId) throws Exception {
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document(memberId);
        DocumentSnapshot documentSnapshot = documentReference.get().get();
        if (!documentSnapshot.exists())
            throw new Exception(); // TODO implement - no member
        else
            return documentReference;
    }

    public List<MemberDto.Response> getAllMembers() throws Exception{
        List<MemberDto.Response> responseList = new ArrayList<>();

        List<QueryDocumentSnapshot> documents =
                firestore.collection(COLLECTION_NAME)
                        .get()
                        .get()
                        .getDocuments();
        for (QueryDocumentSnapshot document : documents)
            responseList.add(document.toObject(MemberDto.Response.class));

        return responseList;
    }
    public MemberDto.Response getMemberDetail(String memberId) throws Exception{
        DocumentSnapshot documentSnapshot = firestore.collection(COLLECTION_NAME)
                .document(memberId)
                .get()
                .get();
        if(documentSnapshot.exists())
            return documentSnapshot.toObject(MemberDto.Response.class);
        else
            return null; // TODO error handling
    }
    public String editMember(String memberId, MemberDto.Request request)
            throws Exception {
        DocumentReference documentReference =
                validateEditMemberRequest(request.getMemberId());
        // TODO 어떤 내용을 editable 하게 설정할 건지 추후 논의 (반드시 id는 변경 불가하도록)
        ApiFuture<WriteResult> apiFuture = documentReference.set(request);
        return apiFuture.get().getUpdateTime().toString();
    }

    public String deleteMember(String memberId) {
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME).document(memberId)
                        .delete();
        return "Document id: " + memberId + " delete";
    }

//    public MemberDto.Response login(MemberDto.login loginMember) {
//        DocumentSnapshot documentSnapshot = firestore.collection(COLLECTION_NAME)
//                .document(memberId)
//                .get()
//                .get();
//        if(documentSnapshot.exists())
//            return documentSnapshot.toObject(MemberDto.Response.class);
//        else
//            return null; // TODO error handling
//    }
}