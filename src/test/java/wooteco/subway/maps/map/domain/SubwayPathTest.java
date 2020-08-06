package wooteco.subway.maps.map.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        LineStationEdge lineStationEdge1 = new LineStationEdge(null, 1L);
        LineStationEdge lineStationEdge2 = new LineStationEdge(null, 2L);
        LineStationEdge lineStationEdge3 = new LineStationEdge(null, 3L);

        List<LineStationEdge> lineStationEdges = new ArrayList<>(Arrays.asList(lineStationEdge1, lineStationEdge2, lineStationEdge3));

        subwayPath = new SubwayPath(lineStationEdges);
    }

    @DisplayName("노선의 id만 추출한다.")
    @Test
    void extractLineIdTest() {
        assertThat(subwayPath.extractLineId()).containsExactly(1L, 2L, 3L);
    }
}