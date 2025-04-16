package com.example.diplom_app.api

import com.example.diplom_app.model.user.CreateCurrentThesisRequest
import com.example.diplom_app.model.user.CreateCurrentThesisResponse
import com.example.diplom_app.model.login.LoginRequest
import com.example.diplom_app.model.login.LoginResponse
import com.example.diplom_app.model.new_request.CreateNewPendingRequestRequest
import com.example.diplom_app.model.new_request.CreateNewPendingRequestResponse
import com.example.diplom_app.model.professors.GetAllProfessorsResponse
import com.example.diplom_app.model.professors.articles.GetAllArticlesRequest
import com.example.diplom_app.model.professors.articles.GetAllArticlesResponse
import com.example.diplom_app.model.professors.previous_theses.GetPreviousThesesRequest
import com.example.diplom_app.model.professors.previous_theses.GetPreviousThesesResponse
import com.example.diplom_app.model.register.RegisterRequest
import com.example.diplom_app.model.register.RegisterResponse
import com.example.diplom_app.model.token.TokenDeleteRequest
import com.example.diplom_app.model.token.TokenValidationRequest
import com.example.diplom_app.model.token.TokenValidationResponse
import com.example.diplom_app.model.user.GetCurrentThesisRequest
import com.example.diplom_app.model.user.GetCurrentThesisResponse
import com.example.diplom_app.model.user.GetPendingRequestsListRequest
import com.example.diplom_app.model.user.GetPendingRequestsListResponse
import com.example.diplom_app.model.user.GetProcessedRequestsListRequest
import com.example.diplom_app.model.user.GetProcessedRequestsListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DiplomApiService {

    @POST("/validate-token")
    fun validateToken(@Body request: TokenValidationRequest): Call<TokenValidationResponse>

    @POST("/delete-token")
    fun deleteToken(@Body request: TokenDeleteRequest)

    @POST("/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/get-current-thesis")
    fun getUserCurrentThesis(@Body request: GetCurrentThesisRequest): Call<GetCurrentThesisResponse>

    @POST("/get-user-pending-requests-list")
    fun getUserPendingTheses(@Body request: GetPendingRequestsListRequest): Call<GetPendingRequestsListResponse>

    @POST("/get-user-processed-requests")
    fun getUserProcessedTheses(@Body request: GetProcessedRequestsListRequest): Call<GetProcessedRequestsListResponse>

    @POST("/create-pending-request")
    fun createPendingRequest(@Body request: CreateNewPendingRequestRequest): Call<CreateNewPendingRequestResponse>

    @POST("/get-all-professors")
    fun getAllProfessors(): Call<GetAllProfessorsResponse>

    @POST("/get-previous-theses")
    fun getPreviousTheses(@Body request: GetPreviousThesesRequest): Call<GetPreviousThesesResponse>

    @POST("/get-all-articles")
    fun getAllArticles(@Body request: GetAllArticlesRequest): Call<GetAllArticlesResponse>

    @POST("/create-current-thesis")
    fun createCurrentThesis(@Body request: CreateCurrentThesisRequest): Call<CreateCurrentThesisResponse>
}