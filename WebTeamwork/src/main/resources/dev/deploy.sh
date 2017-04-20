# Erase everything that was in the web folder
rm -rf ../web/*

# Create a "lib" folder for all of our dependencies
mkdir ../web/lib

# Copy over all of the libraries that we use
cp node_modules/bootstrap/dist/css/bootstrap.min.css ../web/lib
cp node_modules/bootstrap/dist/js/bootstrap.min.js ../web/lib
cp node_modules/jquery/dist/jquery.min.js ../web/lib
cp node_modules/handlebars/dist/handlebars.min.js ../web/lib
cp node_modules/js-cookie/src/js.cookie.js ../web/lib

# Copy Bootstrap's fonts
cp -R node_modules/bootstrap/fonts/ ../web/

# Merge our CSS files into the webed .css
cat article.css content.css index.css nav.css profile.css welcome.css > ../web/phase2web.css

# Compile our TypeScript into the webed .js
# NOTE: It's important that index.ts is last
node_modules/typescript/bin/tsc --outFile ../web/phase2web.js content.ts nav.ts welcome.ts index.ts article.ts profile.ts

# Compile our handlebars templates and append the results into the webed JS
node_modules/handlebars/bin/handlebars content.hb >> ../web/templates.js
node_modules/handlebars/bin/handlebars nav.hb >> ../web/templates.js
node_modules/handlebars/bin/handlebars welcome.hb >> ../web/templates.js
node_modules/handlebars/bin/handlebars article.hb >> ../web/templates.js
node_modules/handlebars/bin/handlebars profile.hb >> ../web/templates.js

# Copy the index.html file and data.json to the webed position
cp index.html ../web

### TODO FOR FRONTEND ###
# add comments section
# finish profile page with all posts and comments listed