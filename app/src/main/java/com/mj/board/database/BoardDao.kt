package com.mj.board.database

import androidx.room.*

@Dao
interface BoardDao {

    //1. 전체 내역 가져오기
    @Query("select * from BoardEntity")
    suspend fun getAll(): MutableList<BoardEntity>

    //2. 제목 검색해서 가져오기
    @Query("select * from BoardEntity where (title || content) like :keyword")
    suspend fun findByTitle(keyword: String): MutableList<BoardEntity>

    //3. 입력하기
    @Insert
    suspend fun insertAll(vararg board: BoardEntity)

    //4. 수정하기
    @Update
    suspend fun modify(board: BoardEntity)

    //5. 삭제하기
    @Delete
    suspend fun delete(board: BoardEntity)

    //6. 특정 uid 기준으로 데이터 불러오기
    @Query("select * from BoardEntity where uid = :uid")
    suspend fun findByUid(uid: Int): BoardEntity

    //7. 최근 내역 가져오기
    @Query("select * from BoardEntity order by uid desc")
    suspend fun getLatest(): MutableList<BoardEntity>

    //8. 색상별 내역 가져오기
    @Query("select * from BoardEntity order by board_color, uid desc")
    suspend fun getByColor(): MutableList<BoardEntity>
}