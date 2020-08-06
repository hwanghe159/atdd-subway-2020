package wooteco.subway.maps.map.dto;

import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations, int highestExtraFare) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, highestExtraFare + 1250 + calculateOverFare(distance - 10));
    }

    public static PathResponse assemble(LoginMember member, SubwayPath subwayPath, Map<Long, Station> stations, int highestExtraFare) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = highestExtraFare + 1250 + calculateOverFare(distance - 10);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, member.discountFare(fare));
    }

    private static int calculateOverFare(int distance) {
        if (distance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
