# Luna Phase 4 Backend

**Edited by Alex Van Heest**

****

**What has been done already for phase 4 backend to update from last working version (version 1.1):**

* Updated backend from phase 1 to include a user-password authentication system (minus OAuth)
* Updated backend from phase 1 to use POSTGRESQL instead of MySQL
* Updated get-data route for new POSTGRESQL schema and added validation functionality
* Updated post-data route for new POSTGRESQL schema and added validation functionality
* Updated up and down vote routes for new POSTGRESQL schema and added validation functionality
* Wrote functionality for validation (no OAuth was completed during phase 3 by backend developer so authentication is basic)
* Wrote get and post routes and functionality for commenting
* Wrote get route for profile page and minimal functionality (due to time restraints)
* Wrote login and logout routes and functionality to check for user sessions and verify information
* Wrote basic hashmap configuration to store user secret keys
* Wrote/updated unit tests
* Diagramming out the backend for teammates on frontend/backend so everyone's route configuration will be consistent.
* Add/edit route for file uploads to GDrive
* Finish configuring file upload functionality

NOTE: I designed backend so that OAuth can easily be added in. Due to time restraints, I won't be adding OAuth during phase 4. Instead, I'll be focusing on getting backend as far along as possible into needed phase 4 functionality.

****

**What still needs to be done:**

* Create a memcachier
* Replace hashmap functionality with memcached functionality
* Use memcached to cache files stored on GDrive
