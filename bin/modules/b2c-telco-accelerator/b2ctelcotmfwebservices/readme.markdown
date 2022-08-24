# TMForum apis & models code generation
### How it works

A new `before_build` macro has been defined in [buildcallbacks.xml](buildcallbacks.xml) that's looking for JSON files in [tmfapis directory](resources/tmfapis).
For each JSON file found `swagger-codegen-cli-{VERSION}.jar` is executed with the following params:
1. `-l spring` - the programming language for which we need the apis generated.
2. `-i ${file}` - the JSON from which we want to generate the apis.
3. `-o gensrc` - the output folder for the new apis. 
4. `-c resources/swagger-generator-config.json` swagger-codgen-cli lib [configuration file](resources/swaggerconfig/v3/swagger-generator-config.json)
5. `--model-package` - the package where the models should be stores including the version
5. `--api-package` - the package where the apis should be stores including the version

### Configuration file
The [configuration file](resources/swaggerconfig/v3/swagger-generator-config.json) allows us to customize general configuration parameters (e.g. apiPackage name, model name prefix/suffix)

It can be customize according to swagger-codgen-cli documentation found [here](https://github.com/swagger-api/swagger-codegen/blob/master/modules/swagger-codegen-maven-plugin/README.md#general-configuration-parameters) 

In order to apply the changes from the configuration file you need to run "ant clean all" 
### Steps to add a new TMF API document

In order to generate the apis from TMForum you need to do the following steps :
1. Go to <https://projects.tmforum.org/wiki/display/API/Open+API+Table>
2. Download the swagger json file that you need and place it in b2ctelcotmfwebservices/resources/tmfapis
3. **MOVE** the "definitions" body from the downloaded file and paste it into [tmf_definitions.json](resources/tmfapis/v2/tmf_definitions.json)
4. #####MAKE SURE YOU HAVE NO DUPLICATED MODELS IN [tmf_definitions.json](resources/tmfapis/v2/tmf_definitions.json) !!!
5. Run "ant clean all" in order to regenerate TMForum apis and models.

At this point you should be ready to start the implementation
### TMForum apis & models customization
In order to add your custom attributes to the APIs you simply need to edit the .json files from 
[tmfapis](resources/tmfapis)

Never delete TMForum attributes as we won't be considered TMForum Compliant!!!. 
We are allowed to :
1. Add new attributes
2. Change the descriptions

