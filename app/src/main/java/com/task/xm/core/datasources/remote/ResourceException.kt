package com.task.xm.core.datasources.remote

import okhttp3.ResponseBody
import java.lang.Exception

/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */

class ApiErrorException(val response: ResponseBody? = null) : Exception()
