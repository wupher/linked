package com.ktvme.linkedin

import com.ktvme.linkedin.util.SecurityUtil
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("DuplicatedCode")
@RequestMapping("/security")
@RestController
class SecurityController {

    @PostMapping("/decrypt")
    fun decrypt(@RequestBody body: Map<String, String>): Map<String, Any> {
        val content = body["content"] ?: return mapOf("ret" to -1, "msg" to "参数错误： content 参数不能为空")
        val key = body["key"] ?: return mapOf("ret" to -1, "msg" to "参数错误： key 参数不能为空")
        val result = try {
            SecurityUtil.caiUnzipAndDecrypt(content, key)
        } catch (e: Exception) {
            e.printStackTrace()
            return mapOf("ret" to -2, "msg" to "解密失败： ${e.localizedMessage}")
        }
        return mapOf("ret" to 0, "msg" to "OK", "result" to result)
    }

    @PostMapping("/encrypt")
    fun encrypt(@RequestBody body: Map<String, String>): Map<String, Any> {
        val content = body["content"] ?: return mapOf("ret" to -1, "msg" to "参数错误： content 参数不能为空")
        val key = body["key"] ?: return mapOf("ret" to -1, "msg" to "参数错误： key 参数不能为空")
        val result = try {
            SecurityUtil.caiZipAndEncrypt(content, key)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return mapOf("ret" to -2, "msg" to "加密失败： ${ex.localizedMessage}")
        }
        return mapOf("ret" to 0, "msg" to "OK", "result" to result)
    }
}