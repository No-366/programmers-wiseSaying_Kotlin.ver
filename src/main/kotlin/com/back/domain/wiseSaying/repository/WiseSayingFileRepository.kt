package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.global.appConfig.AppConfig
import com.back.standard.dto.Page
import com.back.standard.ut.JsonUtil
import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository {

    val tableDirPath: Path
        get(){
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        return wiseSaying
            .takeIf { it.isNew() }
            .also {
                wiseSaying.id = genNextId()
                saveOnDisk(wiseSaying)
            } ?: wiseSaying
    }

    fun saveLastId(lastId: Int){
        mkTableDirsIfNotExists()
        tableDirPath.resolve("lastId.txt")
            .toFile()
            .writeText(lastId.toString())

    }

    fun loadLastId():Int{

//        return try{
//            tableDirPath.resolve("lastId.txt")
//                .toFile()
//                .readText()
//                .toInt()
//        }catch (e: Exception){
//            0
//        }
        return kotlin.runCatching {
            tableDirPath.resolve("lastId.txt")
                .toFile()
                .readText()
                .toInt()
        }.getOrElse { 0 }

    }

    fun genNextId(): Int{
        return (loadLastId() + 1).also{
            saveLastId(it)
        }
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
        return tableDirPath.toFile() // Path -> File로 변환
            .listFiles() // 해당 File을 디렉토리로 보고 안에 있는 파일 목록을 가져온다
            ?.filter{it.name != "data.json"}
            ?.filter{it.name.endsWith(".json")} // 이름이 .json으로 끝나는 파일만 필터링
            ?.map{it.readText()} // 각 JSON파일을 읽어서 문자열로 변환
            ?.map(WiseSaying.Companion::fromJsonStr) // fromJosnStr이라는 정적/컴패니언 함수를 리스트의 각 요소에 적용 -> JSON 문자열이 WiseSaying 객체로 파싱
            ?.sortedByDescending{ it.id }
            .orEmpty() // 이전단계가 null이면 빈 리스트로 대체, 최종적으로 null 이 아닌 리스트를 보장

    }

    override fun findById(id: Int): WiseSaying? {
        return tableDirPath
            .toFile()
            .listFiles()
            ?.find{it.name == "${id}.json"}
            ?.let{WiseSaying.fromJsonStr(it.readText())}
    }

    override fun delete(wiseSaying: WiseSaying) {
        tableDirPath
            .resolve("${wiseSaying.id}.json")
            .toFile()
            .takeIf{it.exists()}
            ?.delete()
    }

    override fun clear() {

    }

    override fun build() {
        mkTableDirsIfNotExists()

        val mapList = findAll()
            .map(WiseSaying::map) // 비즈니스와 유틸의 명확한 구분을 위해 // map 프로퍼티를 꺼내 새로운 리스트로 만닮

        JsonUtil.toString(mapList)
            .let{
                tableDirPath
                    .resolve("data.json")
                    .toFile()
                    .writeText(it)
            }
    }

    private fun filterByKeyword(
        keyword: String,
        selector: (WiseSaying) -> String // "함수를 매개변수로 받는다"는 뜻
    ): List<WiseSaying> {

        val pure = keyword.replace("%", "")
        val wiseSayings = findAll()

        if (pure.isBlank()) return wiseSayings

        return when {
            keyword.startsWith("%") && keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).contains(pure) }
            keyword.startsWith("%") ->
                wiseSayings.filter { selector(it).endsWith(pure) }
            keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).startsWith(pure) }
            else ->
                wiseSayings.filter { selector(it) == pure }
        }
    }

    override fun findByAuthorLike(authorLike: String): List<WiseSaying> {
        return filterByKeyword(authorLike){it.author}
    }

    override fun findByAuthorContent(contentLike: String): List<WiseSaying> {
        return filterByKeyword(contentLike){it.content}// ==> filterByKeyword(contentLike, (wiseSaying> -> {wiseSaying.content})
    }

    override fun findByKeywordPaged(
        keywordType: String,
        keyword: String,
        itemsPerPage: Int,
        pageNo: Int
    ): Page<WiseSaying> {

        val wiseSayings = when (keywordType) {
            "author" -> findByAuthorLike("%$keyword%")
            else -> findByAuthorContent("%$keyword%")
        }

        val content = wiseSayings
            .drop((pageNo - 1) * itemsPerPage)
            .take(itemsPerPage)

        return Page(wiseSayings.size, itemsPerPage, pageNo, keywordType, keyword, content)
    }

    override fun findAllPaged(itemsPerPage: Int, pageNo: Int): Page<WiseSaying> {
        val wiseSayings = findAll()

        val content = wiseSayings
            .drop((pageNo - 1) * itemsPerPage)
            .take(itemsPerPage)

        return Page(wiseSayings.size, itemsPerPage, pageNo, "", "", content)
    }
}