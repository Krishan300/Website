
rm -rf ../deploy/*

mkdir ../deploy/lib

cp node_modules/bootstrap/dist/css/bootstrap.min.css ../deploy/lib
cp node_modules/bootstrap/dist/js/bootstrap.min.js ../deploy/lib
cp node_modules/jquery/dist/jquery.min.js ../deploy/lib
cp node_modules/handlebars/dist/handlebars.min.js ../deploy/lib

cp -R node_modules/bootstrap/fonts/ ../deploy/

cat content.css index.css nav.css login.css welcome.css > ../deploy/webTeamWork.css


node_modules/typescript/bin/tsc --outFile ../deploy/webTeamWork.js content.ts  nav.ts welcome.ts login.ts index.ts commentBox.ts

node_modules/handlebars/bin/handlebars content.hb >> ../deploy/templates.js
node_modules/handlebars/bin/handlebars nav.hb >> ../deploy/templates.js
node_modules/handlebars/bin/handlebars login.hb >> ../deploy/templates.js 
node_modules/handlebars/bin/handlebars welcome.hb >> ../deploy/templates.js
node_modules/handlebars/bin/handlebars commentBox.hb >> ../deploy/templates.js



cp index.html data.json ../deploy