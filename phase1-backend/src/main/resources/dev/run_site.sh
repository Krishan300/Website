# First, run the deploy script
sh deploy.sh

# Then, launch the site with the following command
node_modules/http-serve/bin/http-serve -p 8080 ../deploy/
