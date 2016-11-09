/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package samples.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class XControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private XRepository companies;

    @Test
    public void shallReturnCompanyKeys() throws Exception {
        companies.save(new Company(1));
        companies.save(new Company(2).addKeywords(Arrays.stream(new String[] {"what"})));
        companies.save(new Company(3).addKeywords(Arrays.stream(new String[] {"what", "where"})));

        final String after = LocalDate.now().minusMonths(XService.BACK_TO_THREE_MONTHS).toString();
        this.mockMvc
                .perform(get("/companies").param("created", after).param("keywords", "what"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(2, 3)));

        this.mockMvc
                .perform(get("/companies").param("created", after).param("keywords", "where"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(3)));

        this.mockMvc
                .perform(get("/companies").param("created", after).param("keywords", "how"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        final String now = LocalDate.now().toString();
        this.mockMvc
                .perform(get("/companies").param("created", now).param("keywords", "where"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

}
