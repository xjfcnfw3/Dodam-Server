package com.example.dodam.repository.diary;

import com.example.dodam.domain.diary.DiaryDetail;
import com.example.dodam.domain.diary.DiaryList;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.dodam.domain.diary.Diary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcDiaryRepository {
    private final JdbcTemplate jdbcTemplate;
    public JdbcDiaryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //다이어리 등록
    public Diary save(Diary diary) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("diary").usingGeneratedKeyColumns("id");
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("userId",diary.getUserId());
        parameters.put("date",diary.getDate());
        parameters.put("title",diary.getTitle());
        parameters.put("imgPath",diary.getImgPath());
        parameters.put("oneWord",diary.getTalkToBaby());
        parameters.put("feel",diary.getFeel());
        parameters.put("content",diary.getContent());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        diary.setId(key.longValue());
        return diary;
    }

    //다이어리 수정
    public String updateDiary(Diary diary) {
        Long diaryId = diary.getId();
        jdbcTemplate.update("update diary set title = ?,imgPath = ?,oneWord = ? , feel = ? where id = ?",diary.getTitle(),diary.getImgPath(),diary.getTalkToBaby(),diary.getFeel(),diary.getId());
        return "ok";
    }

    //다이어리 삭제
    public String deleteDiary(Integer id) {
        jdbcTemplate.update("delete from diary where id = ?",id);
        return "ok";
    }


    // 원하는 다이어리 찾기
//    public Optional<Diary>  findByDate(Integer id , String date){
//        List<Diary> result = jdbcTemplate.query("select * from diary where date = ? and userId = ? ",diaryRowmapper(),date,id);
//        return result.stream().findAny();
//    }

    //다이어리 리스트 조회
    public List<DiaryList> findAll(Integer id ) {
        return jdbcTemplate.query("select id,date,feel from diary where userId = ?", diaryListRowmapper(),id);
    }
    //다이어리 조회
    public DiaryDetail findDiary(Integer id ) {
        // jdbcTemplate.queryForObject("select id,title,content,imgPath,oneWord from diary where id = ?", DiaryDetail.class,id);
        List<DiaryDetail> diaryList = jdbcTemplate.query("select id,title,content,imgPath,oneWord from diary where id = ?", diaryDetailRowmapper(),id);
        if (diaryList.size() == 1) {
            return diaryList.get(0);
        }else {
            //예외처리 해야함
            return new DiaryDetail();
        }


    }



//    private RowMapper<Diary> diaryRowmapper(){
//        return (rs, rowNum) -> {
//            Diary diary = new Diary();
//            diary.setId(rs.getLong("id"));
//            diary.setUserId(rs.getLong("userId"));
//            diary.setDate(rs.getDate("date"));
//            diary.setTitle(rs.getString("title"));
//            diary.setFeel(rs.getString("feel"));
//            diary.setOneWord(rs.getString("oneWord"));
//            diary.setImgPath(rs.getString("imgPath"));
//            return diary;
//        };
//    }

    private RowMapper<DiaryList> diaryListRowmapper(){
        return (rs, rowNum) -> {
            DiaryList diary = new DiaryList();
            diary.setId((int) rs.getLong("id"));
            diary.setDate(rs.getDate("date"));
            diary.setFeel(rs.getString("feel"));
            return diary;
        };
    }
    private RowMapper<DiaryDetail> diaryDetailRowmapper(){
        return (rs, rowNum) -> {
            DiaryDetail diary = new DiaryDetail();
            diary.setId(rs.getLong("id"));
            diary.setTitle(rs.getString("title"));
            diary.setContent(rs.getString("content"));
            diary.setImgPath(rs.getString("imgPath"));
            diary.setOneWord(rs.getString("oneWord"));

            return diary;
        };
    }
}
