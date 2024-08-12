package migrations

import app.cash.sqldelight.ColumnAdapter
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class FatherAIEntity(
  public val id: Long,
  public val modelName: String,
  public val title: String,
  public val buttonText: String,
  public val returnType: Int,
  public val isInputNeeded: Boolean,
  public val fileType: Int,
  public val prompt: String,
) {
  public class Adapter(
    public val returnTypeAdapter: ColumnAdapter<Int, Long>,
    public val fileTypeAdapter: ColumnAdapter<Int, Long>,
  )
}
