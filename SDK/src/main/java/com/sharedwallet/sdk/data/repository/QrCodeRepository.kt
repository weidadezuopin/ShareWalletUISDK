package com.sharedwallet.sdk.data.repository

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class QrCodeRepository(
    private val workerDispatcher: CoroutineDispatcher,
) {

    suspend fun generate(text: String): Bitmap = withContext(workerDispatcher) {
        val writer = QRCodeWriter()
        val width = 512
        val height = 512
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        if (text.isBlank()) {
            return@withContext bitmap
        }
        try {
            val bitMatrix: BitMatrix = writer.encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                mapOf(EncodeHintType.MARGIN to 0),
            )
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return@withContext bitmap
    }
}
