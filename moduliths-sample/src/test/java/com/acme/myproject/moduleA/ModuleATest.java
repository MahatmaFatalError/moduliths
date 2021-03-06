/*
 * Copyright 2018 the original author or authors.
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
package com.acme.myproject.moduleA;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.acme.myproject.NonVerifyingModuleTest;
import com.acme.myproject.moduleB.ServiceComponentB;

/**
 * @author Oliver Gierke
 */
@NonVerifyingModuleTest
@RunWith(SpringRunner.class)
public class ModuleATest {

	@Autowired ApplicationContext context;

	@Test
	public void bootstrapsModuleAOnly() {

		context.getBean(ServiceComponentA.class);

		assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
				.isThrownBy(() -> context.getBean(ServiceComponentB.class));
	}
}
