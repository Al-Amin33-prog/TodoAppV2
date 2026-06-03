package com.example.todoappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoappv2.data.local.entity.PriorityTrainingEntity

@Dao
interface PriorityTrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainingSample(
        sample: PriorityTrainingEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainingSamples(
        samples: List<PriorityTrainingEntity>
    )

    @Query("SELECT * FROM priority_training")
    suspend fun getAllTrainingSamples(): List<PriorityTrainingEntity>

    @Query("DELETE FROM priority_training")
    suspend fun clearAll()
    @Query("""
DELETE FROM priority_training
WHERE id NOT IN (
    SELECT id
    FROM priority_training
    ORDER BY id DESC
    LIMIT 500
)
""")
    suspend fun trimDataset()
}