package wooteco.subway.maps.map.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.common.acceptance.AcceptanceTest;
import wooteco.subway.maps.line.acceptance.step.LineAcceptanceStep;
import wooteco.subway.maps.line.dto.LineResponse;
import wooteco.subway.maps.station.acceptance.step.StationAcceptanceStep;
import wooteco.subway.maps.station.dto.StationResponse;

import static wooteco.subway.maps.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static wooteco.subway.maps.map.acceptance.step.PathAcceptanceStep.*;

@DisplayName("지하철 요금 조회")
public class FareAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        교대역 = 지하철역_등록되어_있음("교대역");
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "RED", 0);
        이호선 = 지하철_노선_등록되어_있음("2호선", "GREEN", 500);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "ORANGE", 900);

        지하철_노선에_지하철역_등록되어_있음(이호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 20, 2);

        지하철_노선에_지하철역_등록되어_있음(신분당선, null, 강남역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 15, 1);

        지하철_노선에_지하철역_등록되어_있음(삼호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 20, 2);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 25, 2);
    }

    @DisplayName("두 역의 최단 거리 경로에 해당하는 요금을 조회한다.")
    @Test
    void findFareByShortestDistancePath() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청("DISTANCE", 1L, 3L);

        //then
        적절한_경로를_응답(response, Lists.newArrayList(교대역, 강남역, 양재역));
        총_거리와_소요_시간과_요금을_함께_응답함(response, 35, 3, 2250);
    }


    private Long 지하철역_등록되어_있음(String name) {
        ExtractableResponse<Response> createdStationResponse1 = StationAcceptanceStep.지하철역_등록되어_있음(name);
        return createdStationResponse1.as(StationResponse.class).getId();
    }

    private Long 지하철_노선_등록되어_있음(String name, String color, int extraFare) {
        ExtractableResponse<Response> createLineResponse1 = LineAcceptanceStep.지하철_노선_등록되어_있음(name, color, extraFare);
        return createLineResponse1.as(LineResponse.class).getId();
    }

}
