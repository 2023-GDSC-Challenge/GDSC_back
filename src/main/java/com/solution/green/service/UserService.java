package com.solution.green.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.NonNull;
import com.solution.green.entity.CreateMember;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class UserService {
    public static final String COLLECTION_NAME = "users"; // database name
    public static Firestore firestore = FirestoreClient.getFirestore();

    public String createMember(CreateMember.Request request) throws Exception{
//        validateCreateMemberRequest(request); // TODO
        ApiFuture<WriteResult> apiFuture =
                firestore.collection(COLLECTION_NAME)
                        .document(request.getMemberId().toString()).set(request);
        return apiFuture.get().getUpdateTime().toString();
    }
    private void validateCreateMemberRequest(@NonNull CreateMember.Request request) {
//        developerRepository.findByMemberId(request.getMemberId())
//                .ifPresent((developer -> {
//                    throw new DMakerException(DUPLICATED_MEMBER_ID);
//                }));

//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        DocumentReference docIdRef = rootRef.collection("yourCollection").document(docId);
//        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document exists!");
//                    } else {
//                        Log.d(TAG, "Document does not exist!");
//                    }
//                } else {
//                    Log.d(TAG, "Failed with: ", task.getException());
//                }
//            }
//        });
    }

    public List<CreateMember.Response> getAllMembers() throws Exception{
        List<CreateMember.Response> responseList = new ArrayList<>();

        ApiFuture<QuerySnapshot> future =
                firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            responseList.add(
                    document.toObject(CreateMember.Response.class));
        }

        return responseList;
    }
    public CreateMember getEmployeeDetail() throws Exception{
        DocumentReference documentReference =
                firestore.collection(COLLECTION_NAME).document("1");
        ApiFuture<DocumentSnapshot> apiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = apiFuture.get();
        CreateMember createMember = null;
        if(documentSnapshot.exists()){
            createMember = documentSnapshot.toObject(CreateMember.class);
            return createMember;
        }
        else{
            return null;
        }
    }

//    @Transactional(readOnly = true) // 내부에서 데이터가 바뀌지 않도록 할 수 있음
//    public List<DeveloperDto> getAllEmployedDevelopers() {
//        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
//                .stream() // 컬렉션의 요소를 하나씩 참조, 람다식으로 처리하는 반복자
//                .map(DeveloperDto::fromEntity) // entity -> dto
//                .collect(Collectors.toList()); // 리스트로
//    }
//    @Transactional(readOnly = true)
//    public DeveloperDetailDto getDeveloperDetail(String memberId) {
//        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
//    }
//    private Developer getDeveloperByMemberId(String memberId){
//        // naming convention ** get~ : 반드시 ~가 존재해야함
//        return developerRepository.findByMemberId(memberId)
//                .orElseThrow( () -> new DMakerException(NO_DEVELOPER));
//    }
//    @Transactional(readOnly = true)
//    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
//
//        request.getDeveloperLevel().validateExperienceYears(
//                request.getExperienceYears());
//        // public 메소드에는 큼직한 내용을 담고
//        // private 메소드에 상세 사항을 구현할 것
//
//        // 수정할 때는, entity 로 받아와서 set 으로 수정
//        // @Transactional 가 Dirty Checking 을 동작,
//        // 알아서 DB에 반영해주기 때문에
//        // 별도로 save 는 필요없다.
//        // ** save 는 create, delete 에서만 사용
//        return DeveloperDetailDto.fromEntity(
//                getUpdatedDeveloperFromRequest(request, getDeveloperByMemberId(memberId)));
//    }
//
//    private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
//        developer.setDeveloperLevel(request.getDeveloperLevel());
//        developer.setDeveloperSkillType(request.getDeveloperSkillType());
//        developer.setExperienceYears(request.getExperienceYears());
//
//        return developer;
//    }
//
//    private static void validateDeveloperLevel(
//            DeveloperLevel developerLevel, Integer experienceYears) {
//        developerLevel.validateExperienceYears(experienceYears);
//    }
//
//    @Transactional
//    public DeveloperDetailDto deleteDeveloper(String memberId) {
//        // 1. EMPLOYED -> RETIRED
//        Developer developer = developerRepository.findByMemberId(memberId)
//                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
//        developer.setStatusCode(StatusCode.RETIRED);
//
//        if (developer != null) throw new DMakerException(NO_DEVELOPER);
//
//        // 2. save into RetiredDeveloper
//        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
//                .memberId(memberId)
//                .name(developer.getName())
//                .build();
//        retiredDeveloperRepository.save(retiredDeveloper);
//        return DeveloperDetailDto.fromEntity(developer);
//    }
}