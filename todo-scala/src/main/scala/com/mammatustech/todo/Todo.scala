package com.mammatustech.todo

import io.advantageous.qbit.annotation.Description

@Description("A `TodoItem`.")
class Todo (aName: String, aDescription: String, aCreateTime: Long) {

  @Description("Holds the description of the todo item")
  val description: String = aDescription
  @Description("Holds the name of the todo item")
  val name: String = aName
  val createTime: Long = aCreateTime
  val id: String = name + "::" + createTime


  def canEqual(other: Any): Boolean = other.isInstanceOf[Todo]

  override def equals(other: Any): Boolean = other match {
    case that: Todo =>
      (that canEqual this) &&
        description == that.description &&
        name == that.name &&
        id == that.id &&
        createTime == that.createTime
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(description, name, id, createTime)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}