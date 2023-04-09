package com.example.dodam.service.diary;

import com.example.dodam.domain.diary.*;
import com.example.dodam.domain.diary.dto.AddDiary;
import com.example.dodam.domain.diary.dto.DiaryDetail;
import com.example.dodam.domain.diary.dto.DiaryImage;
import com.example.dodam.domain.diary.dto.DiaryList;
import com.example.dodam.repository.diary.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository ;


    //다이어리 목록 조회
    public List<DiaryList> findDiaries(Integer id ){
//        return diaryRepository.findAll(Long.valueOf(id));
        return null;
    }
    //다이어리 조회
    public Optional<DiaryDetail> findDiary(Integer id ){
        return Optional.of(DiaryDetail.of(diaryRepository.findById(Long.valueOf(id)).get()));
    }

    //다이어리 등록
    public Boolean addDiary(AddDiary diary){
        //중복 다이어리 확인
        validateDuplicateDiary(diary);
        Diary diaryImgpath = diary.toDiary();
        //다이어리 사진 base64 -> img로 저장
        if(diary.getBase64Img() != null){
            byte[] decodeImg  = base64Decode(diary.getBase64Img());
            String target = "/Users/gimga-eun/Desktop/대학/대외활동/도담/Dodam-Server/image/diary/"+"USER"
                +"_"+diary.getDate().toString()+".jpg";
            try {
                BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(decodeImg));
                File file = new File(target);
                //이미지 저장
                ImageIO.write(bufImg, "jpg", file);
                // png일때
                if (!file.exists()) {
                    target = "/Users/gimga-eun/Desktop/대학/대외활동/도담/Dodam-Server/image/diary/"
                        + "USER" + "_" + diary.getDate().toString() + ".png";
                    file = new File(target);
                    ImageIO.write(bufImg, "png", file);
                }
                diaryImgpath.setImgPath(target);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return true;
            }


        }else{
            diaryImgpath.setImgPath(null);
            diaryRepository.save(diaryImgpath);
        }


        return true;
    }

    // 다이어리 수정
    public Integer updateDiary(DiaryImage diary){
        // 받은 base64 이미지 -> 이미지로 변환

        Diary diaryImgpath = diary.toDiary();
        //수정 전에 저장된 주소
        String target = diaryRepository.findById(diary.getId()).get().getImgPath();

        try {
            //이미지를 추가했다면
            if (diary.getBase64Img() != null){
                if(target != null){
                    File deleteFolder = new File(target);
                    deleteFolder.delete();
                }
                byte[] decodeImg  = base64Decode(diary.getBase64Img());
                //수정된 이미지 base64 -> 이미지로 변환
                target = "/Users/gimga-eun/Desktop/대학/대외활동/도담/Dodam-Server/image/diary/" + diary.getUserId().toString() + "_" + diary.getDate().toString() + ".jpg";
                BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(decodeImg));
                File file = new File(target);
                //이미지 저장
                ImageIO.write(bufImg, "jpg", file);
                // png일때
                if (!file.exists()) {
                    target = "/Users/gimga-eun/Desktop/대학/대외활동/도담/Dodam-Server/image/diary/"+diary.getUserId().toString()+"_"+diary.getDate().toString()+".png";
                    file = new File(target);
                    ImageIO.write(bufImg, "png", file);
                }
                diaryImgpath.setImgPath(target);
            }else{
                //이미지가 없다면
                if(target != null){
                    File deleteFolder = new File(target);
                    deleteFolder.delete();
                }
                diaryImgpath.setImgPath(null);
            }
//            diaryRepository.updateDiary(diaryImgpath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

                return 1;
            }
        return 0;
    }

    //다이어리 삭제
    public Integer deleteDiary(Integer id){
        Long diaryId = Long.valueOf(id);
        String target = diaryRepository.findById(diaryId).get().getImgPath();
        File deleteFolder = new File(target);
        deleteFolder.delete();
//        diaryRepository.deleteAllById(diaryId);
        return id;
    }

    // 다이어리 중복 여부 확인
    private void validateDuplicateDiary(AddDiary diary) {
        String diaryDate = diary.getDate().toInstant()
                .atOffset(ZoneOffset.UTC)
                .format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        diaryRepository.findByDate(diaryDate).ifPresent(m->{
            throw new IllegalStateException("이미 존재하는 다이어리");

        });
    }

    // 다이어리 이미지 조회
    public String getImage(String imagePath) throws Exception {
        System.out.println(imagePath);
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

//        String absolutePath = new File("").getAbsolutePath() + "/";
        try {
            inputStream = new FileInputStream(imagePath);
        }
        catch (FileNotFoundException e) {
            throw new Exception("해당 파일을 찾을 수 없습니다.");
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;
        String base64ImgEncode;
        try {
            while((readCount = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, readCount);
            }
            fileArray = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

            Base64.Encoder encoder = Base64.getEncoder();
            byte[] imgEncode = encoder.encode(fileArray);
            base64ImgEncode = new String(imgEncode, "UTF8");



        }
        catch (IOException e) {
            throw new Exception("파일을 변환하는데 문제가 발생했습니다.");
        }

        return base64ImgEncode;
    }
    //base64 디코딩
    public byte[] base64Decode(String base64img){
        Base64.Decoder decode = Base64.getDecoder();
        byte[] decodeImg = decode.decode(base64img);
        return decodeImg;
    }

    //다이어리 이미지 저장
//    public String saveDiaryImag( MultipartFile imgFile) throws Exception{
//        String imagePath = null ;
//        String absolutePath = new File("").getAbsolutePath()+"\\" ;
//        String path = "image/diary";
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        if (!imgFile.isEmpty()) {
//            String contentType = imgFile.getContentType();
//            String originalFileExtension;
//            if (ObjectUtils.isEmpty(contentType)) {
//                throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
//            } else {
//                if (contentType.contains("image/jpeg")) {
//                    originalFileExtension = ".jpg";
//                } else if (contentType.contains("image/png")) {
//                    originalFileExtension = ".png";
//                } else {
//                    throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
//                }
//            }
////            imagePath = path + "/" + "0" + originalFileExtension;
//            imagePath = "/Users/gimga-eun/Desktop/대학/대외활동/도담/Dodam-Server/image/diary/"+ "1" + originalFileExtension;
//            file = new File(imagePath);
//            imgFile.transferTo(file);
//        }
//        else {
//            throw new Exception("이미지 파일이 비어있습니다.");
//        }
//
//        return imagePath;
//    }

}
