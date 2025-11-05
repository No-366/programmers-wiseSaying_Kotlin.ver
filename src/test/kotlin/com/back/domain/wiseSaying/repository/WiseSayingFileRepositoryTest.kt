package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.global.appConfig.AppConfig
import com.back.global.bean.SingletonScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test

class WiseSayingFileRepositoryTest {
    private val wiseSayingRepository = SingletonScope.wiseSayingFileRepository

    companion object{

        @JvmStatic
        @BeforeAll
        fun setMode(){
            AppConfig.setModeToTest()
        }
    }

//    @BeforeEach
//    fun setUp() {
//        wiseSayingRepository.clear()
//    }

    @Test
    fun `save`() {
        val wiseSaying = wiseSayingRepository
            .save(WiseSaying(content = "나의 죽음을 적들에게 알리지 말라.", author = "충무공 이순신"))

        val filePath = wiseSayingRepository
            .tableDirPath // 저장소
            .toFile()
            .listFiles()
            ?.find { it.name == "${wiseSaying.id}.json" }

        assertThat(filePath).isNotNull
    }



}