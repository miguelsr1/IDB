/* 
 * Copyright 2018 xtecuan.
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
/**
 * Author:  xtecuan
 * Created: Jun 29, 2018
 */
CREATE TABLE PUBLIC.PROCESS_EXEC (
	PE_ID INTEGER NOT NULL AUTO_INCREMENT,
	APP_NAME VARCHAR(100) NOT NULL,
	APP_FUNC VARCHAR(100) NOT NULL,
	START_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
	END_DATE TIMESTAMP NOT NULL,
	STATUS VARCHAR(15) NOT NULL,
	"OUTPUT" VARCHAR(2000),
	CONSTRAINT PROCESS_EXEC_PK PRIMARY KEY (PE_ID)
) ;

