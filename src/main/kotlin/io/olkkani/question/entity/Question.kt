package io.olkkani.question.entity

import lombok.AccessLevel
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import javax.persistence.*


//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
class Question (
    @field:GeneratedValue(strategy = GenerationType.AUTO) @field:Id private val id: Long,
    @field:Column private val question: String,
    @field:Column private val optionA: String,
    @field:Column private val optionB: String,
    @field:Column private val answer: String,
    @field:Column private val answerComment: String
)