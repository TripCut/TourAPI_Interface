# 드라마 추천 받기
GET http://localhost:8080/api/v1/recommendations/drama/user123

# 특정 드라마의 촬영지 추천 받기
GET http://localhost:8080/api/v1/recommendations/location/user123/drama456

# 투어 코스 추천 받기
POST http://localhost:8080/api/v1/recommendations/tour-course?userId=user123&dramaIds=drama1,drama2,drama3

# 인기 촬영지 분석 결과 받기
GET http://localhost:8080/api/v1/recommendations/analytics/popular-locations

# 특정 드라마의 분석 결과 받기
GET http://localhost:8080/api/v1/recommendations/analytics/drama/drama456