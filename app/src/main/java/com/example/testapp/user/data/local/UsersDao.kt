package com.example.testapp.user.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testapp.user.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UsersDao {
    @Query(QUERY_ALL)
    abstract fun getAllUsersAsFlow(): Flow<List<UserEntity>>

    @Query(QUERY_ALL_FAVORITE)
    abstract fun getFavoriteUsersAsFlow(): Flow<List<UserEntity>>

    @Query(QUERY_BY_ID)
    abstract fun getUserByIdAsFlow(id: Long): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(users: List<UserEntity>)

    @Query(QUERY_UPDATE_IS_FAVORITE_BY_ID)
    abstract suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean)

    companion object {
        private const val FAVORITE_IS_ENABLED = 1

        internal const val QUERY_ALL = """
        SELECT * FROM ${UserEntity.TABLE_NAME} ORDER BY ${UserEntity.COLUMN_ID} DESC
        """

        internal const val QUERY_ALL_FAVORITE = """
        SELECT * FROM ${UserEntity.TABLE_NAME}
         WHERE ${UserEntity.COLUMN_IS_FAVORITE}=$FAVORITE_IS_ENABLED
        """

        internal const val QUERY_BY_ID = """
            SELECT * FROM ${UserEntity.TABLE_NAME}
            WHERE ${UserEntity.COLUMN_ID}=:id
        """
        internal const val QUERY_UPDATE_IS_FAVORITE_BY_ID = """
            UPDATE ${UserEntity.TABLE_NAME} SET ${UserEntity.COLUMN_IS_FAVORITE} = :isFavorite
            WHERE ${UserEntity.COLUMN_ID}=:id
        """
    }
}