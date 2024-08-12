package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import migrations.FatherAIEntity

public class FatheraiQueries(
  driver: SqlDriver,
  private val FatherAIEntityAdapter: FatherAIEntity.Adapter,
) : TransacterImpl(driver) {
  public fun <T : Any> getLastLocalFatherAIId(mapper: (MAX: Long?) -> T): Query<T> =
      Query(745_441_263, arrayOf("FatherAIEntity"), driver, "fatherai.sq", "getLastLocalFatherAIId",
      "SELECT MAX(id) FROM FatherAIEntity") { cursor ->
    mapper(
      cursor.getLong(0)
    )
  }

  public fun getLastLocalFatherAIId(): Query<GetLastLocalFatherAIId> = getLastLocalFatherAIId {
      MAX ->
    GetLastLocalFatherAIId(
      MAX
    )
  }

  public fun <T : Any> getFatherAIInfoById(id: Long, mapper: (
    id: Long,
    modelName: String,
    title: String,
    buttonText: String,
    returnType: Int,
    isInputNeeded: Boolean,
    fileType: Int,
    prompt: String,
  ) -> T): Query<T> = GetFatherAIInfoByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      FatherAIEntityAdapter.returnTypeAdapter.decode(cursor.getLong(4)!!),
      cursor.getBoolean(5)!!,
      FatherAIEntityAdapter.fileTypeAdapter.decode(cursor.getLong(6)!!),
      cursor.getString(7)!!
    )
  }

  public fun getFatherAIInfoById(id: Long): Query<FatherAIEntity> = getFatherAIInfoById(id) { id_,
      modelName, title, buttonText, returnType, isInputNeeded, fileType, prompt ->
    FatherAIEntity(
      id_,
      modelName,
      title,
      buttonText,
      returnType,
      isInputNeeded,
      fileType,
      prompt
    )
  }

  public fun <T : Any> getFatherAIList(mapper: (
    id: Long,
    modelName: String,
    title: String,
    buttonText: String,
    returnType: Int,
    isInputNeeded: Boolean,
    fileType: Int,
    prompt: String,
  ) -> T): Query<T> = Query(2_110_228_679, arrayOf("FatherAIEntity"), driver, "fatherai.sq",
      "getFatherAIList", "SELECT * FROM FatherAIEntity ORDER BY id DESC") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      FatherAIEntityAdapter.returnTypeAdapter.decode(cursor.getLong(4)!!),
      cursor.getBoolean(5)!!,
      FatherAIEntityAdapter.fileTypeAdapter.decode(cursor.getLong(6)!!),
      cursor.getString(7)!!
    )
  }

  public fun getFatherAIList(): Query<FatherAIEntity> = getFatherAIList { id, modelName, title,
      buttonText, returnType, isInputNeeded, fileType, prompt ->
    FatherAIEntity(
      id,
      modelName,
      title,
      buttonText,
      returnType,
      isInputNeeded,
      fileType,
      prompt
    )
  }

  public fun <T : Any> getFatherAIListByOffset(
    limit: Long,
    offset: Long,
    mapper: (
      id: Long,
      modelName: String,
      title: String,
      buttonText: String,
      returnType: Int,
      isInputNeeded: Boolean,
      fileType: Int,
      prompt: String,
    ) -> T,
  ): Query<T> = GetFatherAIListByOffsetQuery(limit, offset) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      FatherAIEntityAdapter.returnTypeAdapter.decode(cursor.getLong(4)!!),
      cursor.getBoolean(5)!!,
      FatherAIEntityAdapter.fileTypeAdapter.decode(cursor.getLong(6)!!),
      cursor.getString(7)!!
    )
  }

  public fun getFatherAIListByOffset(limit: Long, offset: Long): Query<FatherAIEntity> =
      getFatherAIListByOffset(limit, offset) { id, modelName, title, buttonText, returnType,
      isInputNeeded, fileType, prompt ->
    FatherAIEntity(
      id,
      modelName,
      title,
      buttonText,
      returnType,
      isInputNeeded,
      fileType,
      prompt
    )
  }

  public fun countFatherAI(): Query<Long> = Query(381_739_394, arrayOf("FatherAIEntity"), driver,
      "fatherai.sq", "countFatherAI", "SELECT count(*) FROM FatherAIEntity") { cursor ->
    cursor.getLong(0)!!
  }

  public fun insertFatherAI(
    id: Long?,
    modelName: String,
    title: String,
    buttonText: String,
    returnType: Int,
    isInputNeeded: Boolean,
    fileType: Int,
    prompt: String,
  ) {
    driver.execute(-1_054_893_714, """
        |INSERT INTO FatherAIEntity (id, modelName, title, buttonText, returnType, isInputNeeded, fileType, prompt)
        |VALUES (?,?, ?,?,?,?,?,?)
        """.trimMargin(), 8) {
          bindLong(0, id)
          bindString(1, modelName)
          bindString(2, title)
          bindString(3, buttonText)
          bindLong(4, FatherAIEntityAdapter.returnTypeAdapter.encode(returnType))
          bindBoolean(5, isInputNeeded)
          bindLong(6, FatherAIEntityAdapter.fileTypeAdapter.encode(fileType))
          bindString(7, prompt)
        }
    notifyQueries(-1_054_893_714) { emit ->
      emit("FatherAIEntity")
    }
  }

  public fun updateFatherAI(
    modelName: String,
    title: String,
    buttonText: String,
    returnType: Int,
    isInputNeeded: Boolean,
    fileType: Int,
    prompt: String,
    fatherAIID: Long,
  ) {
    driver.execute(-1_507_852_418, """
        |UPDATE FatherAIEntity SET modelName = ?,  title = ?, buttonText = ?,
        | returnType = ?, isInputNeeded = ?, fileType = ?, prompt = ? WHERE id = ?
        """.trimMargin(), 8) {
          bindString(0, modelName)
          bindString(1, title)
          bindString(2, buttonText)
          bindLong(3, FatherAIEntityAdapter.returnTypeAdapter.encode(returnType))
          bindBoolean(4, isInputNeeded)
          bindLong(5, FatherAIEntityAdapter.fileTypeAdapter.encode(fileType))
          bindString(6, prompt)
          bindLong(7, fatherAIID)
        }
    notifyQueries(-1_507_852_418) { emit ->
      emit("FatherAIEntity")
    }
  }

  public fun deleteFatherAI(fatherAIID: Long) {
    driver.execute(-136_033_696, """DELETE FROM FatherAIEntity WHERE id = ?""", 1) {
          bindLong(0, fatherAIID)
        }
    notifyQueries(-136_033_696) { emit ->
      emit("FatherAIEntity")
    }
  }

  private inner class GetFatherAIInfoByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("FatherAIEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("FatherAIEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_076_325_559, """SELECT * FROM FatherAIEntity WHERE id = ?""", mapper,
        1) {
      bindLong(0, id)
    }

    override fun toString(): String = "fatherai.sq:getFatherAIInfoById"
  }

  private inner class GetFatherAIListByOffsetQuery<out T : Any>(
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("FatherAIEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("FatherAIEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_200_769_615, """SELECT * FROM FatherAIEntity LIMIT ? OFFSET ?""",
        mapper, 2) {
      bindLong(0, limit)
      bindLong(1, offset)
    }

    override fun toString(): String = "fatherai.sq:getFatherAIListByOffset"
  }
}
