package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;

import io.swagger.client.model.*;

import java.util.*;

import io.swagger.client.model.Department;
import io.swagger.client.model.Employee;
import io.swagger.client.model.PhoneNumber;

import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class DefaultApi {
  private ApiClient apiClient;

  public DefaultApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DefaultApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  
  /**
   * 
   * 
   * @return List<Department>
   */
  public List<Department> getDepartments () throws ApiException {
    Object postBody = null;
    

    // create path and map variables
    String path = "/hr/department/".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (List<Department>) apiClient.deserialize(response, "array", Department.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * 
   * @param departmentId 
   * @param body 
   * @return Boolean
   */
  public Boolean addDepartment (Integer departmentId, Department body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'departmentId' is set
    if (departmentId == null) {
       throw new ApiException(400, "Missing the required parameter 'departmentId' when calling addDepartments");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
       throw new ApiException(400, "Missing the required parameter 'body' when calling addDepartments");
    }
    

    // create path and map variables
    String path = "/hr/department/{departmentId}/".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "departmentId" + "\\}", apiClient.escapeString(departmentId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "POST", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (Boolean) apiClient.deserialize(response, "", Boolean.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * 
   * @param departmentId 
   * @param body 
   * @return Boolean
   */
  public Boolean addEmployee (Integer departmentId, Employee body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'departmentId' is set
    if (departmentId == null) {
       throw new ApiException(400, "Missing the required parameter 'departmentId' when calling addEmployee");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
       throw new ApiException(400, "Missing the required parameter 'body' when calling addEmployee");
    }
    

    // create path and map variables
    String path = "/hr/department/{departmentId}/employee/".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "departmentId" + "\\}", apiClient.escapeString(departmentId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "POST", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (Boolean) apiClient.deserialize(response, "", Boolean.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * 
   * @param departmentId 
   * @param employeeId 
   * @return Employee
   */
  public Employee getEmployee (Integer departmentId, Integer employeeId) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'departmentId' is set
    if (departmentId == null) {
       throw new ApiException(400, "Missing the required parameter 'departmentId' when calling getEmployee");
    }
    
    // verify the required parameter 'employeeId' is set
    if (employeeId == null) {
       throw new ApiException(400, "Missing the required parameter 'employeeId' when calling getEmployee");
    }
    

    // create path and map variables
    String path = "/hr/department/{departmentId}/employee/{employeeId}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "departmentId" + "\\}", apiClient.escapeString(departmentId.toString()))
      .replaceAll("\\{" + "employeeId" + "\\}", apiClient.escapeString(employeeId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (Employee) apiClient.deserialize(response, "", Employee.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * 
   * @param departmentId 
   * @param employeeId 
   * @return List<PhoneNumber>
   */
  public List<PhoneNumber> getPhoneNumbers (Integer departmentId, Integer employeeId) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'departmentId' is set
    if (departmentId == null) {
       throw new ApiException(400, "Missing the required parameter 'departmentId' when calling getPhoneNumbers");
    }
    
    // verify the required parameter 'employeeId' is set
    if (employeeId == null) {
       throw new ApiException(400, "Missing the required parameter 'employeeId' when calling getPhoneNumbers");
    }
    

    // create path and map variables
    String path = "/hr/department/{departmentId}/employee/{employeeId}/phoneNumber/".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "departmentId" + "\\}", apiClient.escapeString(departmentId.toString()))
      .replaceAll("\\{" + "employeeId" + "\\}", apiClient.escapeString(employeeId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (List<PhoneNumber>) apiClient.deserialize(response, "array", PhoneNumber.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * 
   * @param departmentId 
   * @param employeeId 
   * @param body 
   * @return Boolean
   */
  public Boolean addPhoneNumber (Integer departmentId, Integer employeeId, PhoneNumber body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'departmentId' is set
    if (departmentId == null) {
       throw new ApiException(400, "Missing the required parameter 'departmentId' when calling addPhoneNumber");
    }
    
    // verify the required parameter 'employeeId' is set
    if (employeeId == null) {
       throw new ApiException(400, "Missing the required parameter 'employeeId' when calling addPhoneNumber");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
       throw new ApiException(400, "Missing the required parameter 'body' when calling addPhoneNumber");
    }
    

    // create path and map variables
    String path = "/hr/department/{departmentId}/employee/{employeeId}/phoneNumber/".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "departmentId" + "\\}", apiClient.escapeString(departmentId.toString()))
      .replaceAll("\\{" + "employeeId" + "\\}", apiClient.escapeString(employeeId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      
      if(hasFields)
        postBody = mp;
    }
    else {
      
    }

    try {
      String[] authNames = new String[] {  };
      String response = apiClient.invokeAPI(path, "POST", queryParams, postBody, headerParams, formParams, accept, contentType, authNames);
      if(response != null){
        return (Boolean) apiClient.deserialize(response, "", Boolean.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
}
