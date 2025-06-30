package com.tripcut.domain.drama.constant;

public final class DramaConstants {
    
    // Drama 관련 상수
    public static final String DRAMA_CONVERT_PARAM = "DRAMA_CONVERT_PARAM";
    public static final String DRAMA_CREATE_PARAM = "DRAMA_CREATE_PARAM";
    public static final String DRAMA_UPDATE_PARAM = "DRAMA_UPDATE_PARAM";
    public static final String DRAMA_DELETE_PARAM = "DRAMA_DELETE_PARAM";
    
    // Drama Review 관련 상수
    public static final String DRAMA_REVIEW_CONVERT_PARAM = "DRAMA_REVIEW_CONVERT_PARAM";
    public static final String DRAMA_REVIEW_CREATE_PARAM = "DRAMA_REVIEW_CREATE_PARAM";
    public static final String DRAMA_REVIEW_UPDATE_PARAM = "DRAMA_REVIEW_UPDATE_PARAM";
    
    // Drama 검색 관련 상수
    public static final String DRAMA_SEARCH_BY_GENRE = "DRAMA_SEARCH_BY_GENRE";
    public static final String DRAMA_SEARCH_BY_YEAR = "DRAMA_SEARCH_BY_YEAR";
    public static final String DRAMA_SEARCH_BY_STATION = "DRAMA_SEARCH_BY_STATION";
    public static final String DRAMA_SEARCH_BY_KEYWORD = "DRAMA_SEARCH_BY_KEYWORD";
    public static final String DRAMA_SEARCH_BY_LOCATION = "DRAMA_SEARCH_BY_LOCATION";
    
    // Drama 통계 관련 상수
    public static final String DRAMA_AVERAGE_RATING = "DRAMA_AVERAGE_RATING";
    public static final String DRAMA_REVIEW_COUNT = "DRAMA_REVIEW_COUNT";
    public static final String DRAMA_TOP_RATED = "DRAMA_TOP_RATED";
    
    // 기본값 상수
    public static final int DEFAULT_TOP_RATED_LIMIT = 10;
    public static final int DEFAULT_REVIEW_RATING_MIN = 1;
    public static final int DEFAULT_REVIEW_RATING_MAX = 5;
    
    // 에러 메시지 상수
    public static final String ERROR_DRAMA_NOT_FOUND = "Drama not found with id: ";
    public static final String ERROR_REVIEW_NOT_FOUND = "Review not found with id: ";
    public static final String ERROR_USER_NOT_FOUND = "User not found with id: ";
    
    // 메트릭 이름 상수
    public static final String METRIC_DRAMA_CREATE = "drama.create";
    public static final String METRIC_DRAMA_GET_BY_ID = "drama.getById";
    public static final String METRIC_DRAMA_GET_ALL = "drama.getAll";
    public static final String METRIC_DRAMA_UPDATE = "drama.update";
    public static final String METRIC_DRAMA_DELETE = "drama.delete";
    public static final String METRIC_DRAMA_SEARCH = "drama.searchByKeyword";
    public static final String METRIC_DRAMA_GET_BY_GENRE = "drama.getByGenre";
    public static final String METRIC_DRAMA_GET_BY_YEAR = "drama.getByYear";
    public static final String METRIC_DRAMA_GET_BY_STATION = "drama.getByStation";
    public static final String METRIC_DRAMA_GET_BY_LOCATION = "drama.getByFilmingLocation";
    public static final String METRIC_DRAMA_GET_TOP_RATED = "drama.getTopRated";
    public static final String METRIC_DRAMA_GET_LOCATIONS = "drama.getFilmingLocations";
    public static final String METRIC_DRAMA_CREATE_REVIEW = "drama.createReview";
    public static final String METRIC_DRAMA_GET_REVIEWS = "drama.getReviews";
    public static final String METRIC_DRAMA_UPDATE_REVIEW = "drama.updateReview";
    public static final String METRIC_DRAMA_DELETE_REVIEW = "drama.deleteReview";
    public static final String METRIC_DRAMA_GET_AVERAGE_RATING = "drama.getAverageRating";
    public static final String METRIC_DRAMA_GET_REVIEW_COUNT = "drama.getReviewCount";
    
    // TourAPI 서비스 상수
    public static final String TOUR_API_SERVICE_DRAMA = "drama";
    public static final String TOUR_API_OPERATION_CREATE = "create";
    public static final String TOUR_API_OPERATION_DETAIL = "detail";
    public static final String TOUR_API_OPERATION_LIST = "list";
    public static final String TOUR_API_OPERATION_UPDATE = "update";
    public static final String TOUR_API_OPERATION_DELETE = "delete";
    public static final String TOUR_API_OPERATION_SEARCH = "search";
    
    private DramaConstants() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }
} 