package com.mj.board.database

import androidx.room.*

@Dao
interface BoardDao {

    //1. 전체 내역 가져오기
    @Query("select * from BoardEntity")
    suspend fun getAll(): MutableList<BoardEntity>

    //2. 제목 검색해서 가져오기
    @Query("select * from boardentity where title like :title")
    suspend fun findByTitle(title: String): MutableList<BoardEntity>

    //3. 입력하기
    @Insert
    suspend fun insertAll(vararg board: BoardEntity)

    //4. 수정하기
    @Update
    suspend fun modify(board: BoardEntity)

    //5. 삭제하기
    @Delete
    suspend fun delete(board: BoardEntity)

}