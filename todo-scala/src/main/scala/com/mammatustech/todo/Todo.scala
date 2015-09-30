package com.mammatustech.todo

import io.advantageous.qbit.annotation.Description

@Description("A `TodoItem`.")
case class Todo (name: String, description: String, createTime: Long) {
  def getName = { this.name }
  def getDescription = {this.description}
  def getCreateTime = { this.createTime }
  val id = s"$name-$createTime"
  def getId = {this.id}
}