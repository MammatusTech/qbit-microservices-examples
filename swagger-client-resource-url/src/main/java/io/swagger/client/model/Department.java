package io.swagger.client.model;

import io.swagger.client.model.Employee;
import java.util.*;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
public class Department  {
  
  private Long id = null;
  private List<Employee> employeeList = new ArrayList<Employee>();

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("employeeList")
  public List<Employee> getEmployeeList() {
    return employeeList;
  }
  public void setEmployeeList(List<Employee> employeeList) {
    this.employeeList = employeeList;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Department {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  employeeList: ").append(employeeList).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
