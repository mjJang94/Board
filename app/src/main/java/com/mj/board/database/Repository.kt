package com.mj.board.database

import android.content.Context
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(context: Context){

    private val boardDatabase = BoradDB.getInstance(context.applicationContext)
    private val boardDao = boardDatabase!!.boardDao()


    suspend fun getByLatestData(): MutableList<BoardEntity>{
        return boardDao.getLatest()
    }
    suspend fun getByColorData(): MutableList<BoardEntity>{
        return boardDao.getByColor()
    }

    suspend fun findBoardsByTitle(keyword: String): MutableList<BoardEntity>{
        return boardDao.findByTitle(keyword)
    }

    suspend fun insertBoard(boardEntity: BoardEntity){
        boardDao.insertAll(boardEntity)
    }

    suspend fun modifyBoard(boardEntity: BoardEntity){
        boardDao.modify(boardEntity)
    }

    suspend fun deleteBoard(boardEntity: BoardEntity){
        boardDao.delete(boardEntity)
    }

    suspend fun findBoardByUid(uid: Int): BoardEntity{
        return boardDao.findByUid(uid)
    }


}