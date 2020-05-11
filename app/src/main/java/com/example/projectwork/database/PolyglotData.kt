/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.projectwork.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "studied_words")
data class PolyglotData(
        @PrimaryKey(autoGenerate = true)
        val uniqueId: Long,

        @ColumnInfo(name = "studied_word_ids")
        var studiedWordIds: String = "",

        @ColumnInfo(name = "not_studied_word_ids")
        var notStudiedWordIds: String = "",

        @ColumnInfo(name = "studied_original_words")
        var studiedOriginalWords: String = "",

        @ColumnInfo(name = "not_studied_original_words")
        var notStudiedOriginalWords: String = "",

        @ColumnInfo(name = "all_count")
        var allCount: Long = 0L,

        @ColumnInfo(name = "studied_count")
        var studiedCount: Long = 0L,

        @ColumnInfo(name = "not_studied_count")
        var notStudiedCount: Long = 0L
)