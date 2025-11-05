package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.global.appConfig.AppConfig
import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository {

    var lastId = 0
    val wiseSayings = mutableListOf<WiseSaying>()

    val tableDirPath: Path
        get(){
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        return wiseSaying
            .takeIf { it.isNew() }
            .also {
                wiseSaying.id = ++lastId
                saveOnDisk(wiseSaying)
            } ?: wiseSaying
    }

    // 실제 파일 저장 메서드
    private fun saveOnDisk(wiseSaying: WiseSaying) {
        mkTableDirsIfNotExists()

        val wiseSayingFile = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFile.toFile().writeText(wiseSaying.jsonStr)
    }

    // 해당 경로에 파일이 있는지 확인 메서드
    private fun mkTableDirsIfNotExists() {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun findAll(): List<WiseSaying> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): WiseSaying? {
        TODO("Not yet implemented")
    }

    override fun delete(wiseSaying: WiseSaying): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}