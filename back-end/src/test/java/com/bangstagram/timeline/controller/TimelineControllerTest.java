package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimelineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TimelineRepository timelineRepository;

    @BeforeEach
    void setUp() throws Exception {
        timelineRepository.deleteAll();
    }

    // 타임라인 create 로직 테스트
    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 만들기: 모든 데이터 잘 들어갔을 때")
    public void isSuccessCreateTimelineWithGoodData() throws Exception {
        String goodJsonData = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        MvcResult result = mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodJsonData))
                .andExpect(status().isOk())
                .andDo(document("timeline/create",
                        responseFields(
                                fieldWithPath("id").description("timeline id"),
                                fieldWithPath("title").description("title"),
                                fieldWithPath("body").description("body"),
                                fieldWithPath("userId").description("userId"),
                                fieldWithPath("roomId").description("roomId"),
                                fieldWithPath("createdAt").description("created time")
                        )
                ))
                .andReturn();
        TimelineResponseDto newTimelineResponse
                = mapper.readValue(result.getResponse().getContentAsString(), TimelineResponseDto.class);

        assertThat(newTimelineResponse.getBody()).isEqualTo("hoon");
        assertThat(newTimelineResponse.getTitle()).isEqualTo("hoon");
        assertThat(newTimelineResponse.getUserId()).isEqualTo(1);
        assertThat(newTimelineResponse.getRoomId()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 만들기: Title 없을 때")
    public void isFailCreateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutTitle))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 만들기: Body 없을 때")
    public void isFailCreateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\n\t\"title\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 만들기: UserId 없을 때")
    public void isFailCreateTimelineWithoutUserId() throws Exception {
        String jsonDataWithoutUserId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"roomId\": 1\n}";
        mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutUserId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 만들기: RoomId 없을 때")
    public void isFailCreateTimelineWithoutRoomId() throws Exception {
        String jsonDataWithoutRoomId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1}";
        mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutRoomId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    // 타임라인 Get 로직 테스트
    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 가져오기 : 데이터 만들고 잘 찾아오는지 체크")
    public void isSuccessFindAllTimelineRelatedUserId() throws Exception {
        JSONObject jsonDummy = new JSONObject();
        jsonDummy.put("title", "testTitle");
        jsonDummy.put("body", "testBody");
        jsonDummy.put("roomId", 1);
        jsonDummy.put("userId", (long) 1);
        MvcResult createdResult = mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDummy.toString()))
                .andExpect(status().isOk())
                .andReturn();

        TimelineResponseDto madeTimeline
                = mapper.readValue(createdResult.getResponse().getContentAsString(), TimelineResponseDto.class);

        assertThat(madeTimeline.getTitle()).isEqualTo(madeTimeline.getTitle());

        MvcResult getResult = mockMvc.perform(get("/timelines" + "/" + madeTimeline.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<TimelineResponseDto> newTimelineList
                = mapper.readValue(getResult.getResponse().getContentAsString(), new TypeReference<List<TimelineResponseDto>>() {});

        System.out.println(madeTimeline);
        System.out.println(newTimelineList);

        assertThat(newTimelineList.size()).isEqualTo(1);
        assertThat(newTimelineList.get(0).getTitle()).isEqualTo("testTitle");
        assertThat(newTimelineList.get(0).getBody()).isEqualTo("testBody");
        assertThat(newTimelineList.get(0).getRoomId()).isEqualTo(1L);
        assertThat(newTimelineList.get(0).getUserId()).isEqualTo((long) 1);
    }

    // 타임라인 Update 로직 테스트
    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 업데이트: 해당 Id가 존재 + 모든 데이터 존재할 경우")
    public void isSuccessUpdateTimeline() throws Exception {
        String createNewTimelineJson = "{\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        MvcResult createResult = mockMvc.perform(post("/timelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createNewTimelineJson))
                .andReturn();
        TimelineResponseDto newTimeline
                = mapper.readValue(createResult.getResponse().getContentAsString(), TimelineResponseDto.class);
        String requestUpdateUrlPath = "/timelines/" + newTimeline.getId();

        String updateJson = "{\"title\": \"new\",\n\t\"body\": \"new\"}";
        MvcResult updateResult = mockMvc.perform(put(requestUpdateUrlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        TimelineResponseDto newTimelineResponse
                = mapper.readValue(updateResult.getResponse().getContentAsString(), TimelineResponseDto.class);

        assertThat(newTimelineResponse.getBody()).isEqualTo("new");
        assertThat(newTimelineResponse.getTitle()).isEqualTo("new");
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 업데이트: 존재하지 않는 id 업데이트를 한 경우")
    public void isFailDoNotExistTimelineId() throws Exception {
        String doNotExistTimeline = "/timelines/987654321";
        String goodJsonData = "{\"title\": \"new\",\n\t\"body\": \"new\"}";
        mockMvc.perform(put(doNotExistTimeline)
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodJsonData))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 업데이트: Title 없을 때")
    public void isFailUpdateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\"}";
        mockMvc.perform(put("/timelines/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutTitle))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 업데이트: Body 없을 때")
    public void isFailUpdateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\"title\": \"new\"}";
        mockMvc.perform(put("/timelines/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataWithoutBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}