package wooteco.subway.maps.map.documentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.application.PathService;
import wooteco.subway.maps.map.ui.PathController;
import wooteco.subway.members.favorite.ui.FavoriteController;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebMvcTest(controllers = {PathController.class})
public class PathDocumentation extends Documentation {
    @MockBean
    private MapService mapService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @Test
    void findPathWithToken() {
        given().log().all().
                header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DISTANCE").
                then().
                log().all().
                apply(document("paths/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        requestParameters(
                                parameterWithName("source").description("출발 역 아이디"),
                                parameterWithName("target").description("도착 역 아이디"),
                                parameterWithName("type").description("타입")
                        ),

                        responseFields(
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 시간"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                                fieldWithPath("fare").type(JsonFieldType.OBJECT).description("요금")))).
                extract();
    }
}
