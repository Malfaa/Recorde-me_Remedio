package com.malfaa.recorde_me_remedio.repository

import android.util.Log
import android.widget.Toast
import com.malfaa.recorde_me_remedio.local.Remedio


class FakeRepositoryTest : IRepository {

    var reminderList = mutableListOf<Remedio>()

    fun getRecebeItem() : List<Remedio>{
        return ArrayList(reminderList)
    }

    override suspend fun adicionandoRemedio(item: Remedio) {
        reminderList.add(item)
    }

    override suspend fun alterarRemedio(item: Remedio) {
    }

    override suspend fun deletarRemedio(item: Remedio) {
        reminderList.clear()
    }
}