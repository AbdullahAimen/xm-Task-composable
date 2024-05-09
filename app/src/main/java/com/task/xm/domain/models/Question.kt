package com.task.xm.domain.models

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */

data class Question(val id: Int?, val questionText: String?, var answer: String?, var submitted: Boolean = false)