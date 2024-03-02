package wortk.my.portfolio.spring_jms_client.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import wortk.my.portfolio.spring_jms_client.service.VideoEncodeService;

@WebMvcTest(VideoEncodeClientController.class)
class VideoEncodeClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VideoEncodeService service;

	@Test
	void test_displayInputView() throws Exception {
		mockMvc.perform(//
				get("/videoEncoderInput")//
		)//
				.andExpect(status().isOk())//
				.andExpect(view().name("videoEncoderInput"))//
				.andExpect(content().string(containsString("動画変換")));
	}

	@Test
	void test_displayOutputViewForGet() throws Exception {
		mockMvc.perform(//
				get("/videoEncoderOutput")//
		)//
				.andExpect(status().is3xxRedirection())//
				.andExpect(redirectedUrl("/videoEncoderInput"));
	}

	@Test
	void test_displayOutputView() throws Exception {
		mockMvc.perform(//
				post("/videoEncoderOutput")//
		)//
				.andExpect(status().isOk())//
				.andExpect(model().attributeExists("videoModel"));
	}

	@Test
	void test_delete() throws Exception {
		mockMvc.perform(//
				delete("/uploadFile/{videoName}", "testName")//
		)//
				.andExpect(status().isOk());
	}

}
