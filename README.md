# travel-behavior-analysis
Java application to process travel behavior data collected by OneBusAway and stored in Firebase Firestore.

## Setup
1. Generate a private key file for your service account.
To generate the key file follow the instructions in Firebase [setup page](https://firebase.google.com/docs/admin/setup).
2. Rename the generated file to `admin-key.json`.
3. Put `admin-key.json` under the resources folder.
4. Run the application by typing `mvn compile`.
5. The application will generate a `travel-behavior.csv` file under the `ravel-behavior-analysis/target/classes/` folder.

## License

```
/*
 * Copyright (C) 2019 University of South Florida
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
 ```
