package io.olkkani.common.util

import mu.KotlinLogging
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*


val log = KotlinLogging.logger {}
class ImageFile {
//    private val log: Logger = LoggerFactory.getLogger(ImageFile::class.java)
//    inline fun <reified T> logger() : log = LoggerFactory.getLogger(T::class.java)

    private val savedImageFilePath = "/Users/acj/Documents/images"

    private fun fileNameToHash (fileName: String) : String {
        var hashedFileName : String = fileName
        try {
            val random : SecureRandom = SecureRandom.getInstance("SHA1PRNG")
//            var bytes : Array<Byte?> = arrayOfNulls(16)
            var bytes = ByteArray(16)
            random.nextBytes(bytes)
            // salt 생성
            val salt : String = Base64.getEncoder().encode(bytes).toString()
            hashedFileName = fileName + salt
            // sha-256 + salt hash
            val md : MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update(hashedFileName.toByteArray())
            return String.format("%064x", BigInteger(1, md.digest()))
        } catch (e: NoSuchAlgorithmException){
            log.error("not found algorithm, please check algorithm get instance", e);
        }
        return hashedFileName
    }

    private fun isDuplicateFileName (fileName: String) : Boolean {
        val file : File = File(fileName)
        return !file.exists()
    }

    private fun saveImageFile (fileName: String, imageFile: MultipartFile) {
        try {
            imageFile.transferTo(File(fileName))
        } catch (e: IOException) {
            log.error("",e);
        }
    }

    fun saveFileAndReturnHashedFileName(imageFile: MultipartFile): String? {
        val originalFileName = imageFile.originalFilename
        val extension: String = FilenameUtils.getExtension(originalFileName)
        var fileFullName: String
        var hashedFileName: String
        do {
            hashedFileName = fileNameToHash(FilenameUtils.getBaseName(originalFileName))
            fileFullName = Paths.get(savedImageFilePath, "$hashedFileName.$extension").toString()
        } while (isDuplicateFileName(fileFullName))
        saveImageFile(fileFullName, imageFile)
        return "$hashedFileName.$extension"
    }

    fun getMultipartFileByFileName(fileName: String?): MultipartFile? {
        val file: File = Paths.get(savedImageFilePath, fileName).toFile()
        var fileItem: FileItem? = null
        try {
            fileItem = DiskFileItem(
                fileName,
                Files.probeContentType(file.toPath()),
                false,
                file.name,
                file.length().toInt(),
                file.parentFile
            )
            IOUtils.copy(FileInputStream(file), fileItem.getOutputStream())
        } catch (ex: IOException) {
            log.error("file not found", ex)
        }
        assert(fileItem != null)
        return fileItem?.let { CommonsMultipartFile(it) }
    }

    fun deleteImageFile(fileName: String?) {
        try {
            Files.deleteIfExists(Paths.get(savedImageFilePath, fileName).toAbsolutePath())
        } catch (e: IOException) {
            log.error("file has already been deleted", e)
        }
    }
}