//http://wiki.jikexueyuan.com/project/gradle-2-user-guide/using-the-gradle-command-line.html

1.因为每个目录下只能有一个build.gradle文件。
为了方便测试，通过setting.gradle管理每个目录下的build.gradle文件，在setting.gradle所在目录下测试即可。

2.gradle logging 文档：https://docs.gradle.org/current/userguide/logging.html

3.多Task时执行每个Task的顺序和依赖关系有关。

4.执行gradle 命令时,默认情况下总是会在当前目录下寻找构建文件
（译者注：首先会寻找当前目录下的 build.gradle 文件,以及根据settings.gradle 中的配置寻找子项目的 build.gradle ）。
可以使用 -b 参数选择其他的构建文件,并且当你使用此参数时 settings.gradle 将不会被使用,看下面的例子:

5.选择要执行的构建
调用 gradle 命令时,默认情况下总是会在当前目录下寻找构建文件（译者注：首先会寻找当前目录下的 build.gradle 文件,
以及根据settings.gradle 中的配置寻找子项目的 build.gradle ）。
可以使用 -b 参数选择其他的构建文件,并且当你使用此参数时 settings.gradle 将不会被使用。
eg:  gradle -b task/mytask.gradle aaaB
或者，您可以使用 -p 选项来指定要使用的项目目录。多 project 的构建时应使用 -p 选项来代替 -b 选项。
gradle -p task aaaB

6.获取构建信息
Gradle 提供了许多内置 task 来收集构建信息。这些内置 task 对于了解依赖结构以及解决问题都是很有帮助的。

7.项目列表
gradle projects

8.任务列表
执行 gradle tasks 会列出项目中所有 task。这份报告显示项目中所有的默认 task 以及每个 task 的描述。
gradle tasks --all 会列出项目中所有被主 task 的分组的 task 以及 task 之间的依赖关系。

9.显示 task 使用细节
gradle help --task compile

10.依赖列表
执行 gradle dependencies 会列出#项目#的依赖列表,所有依赖会根据任务区分,以树型结构展示出来。

11.查看特定依赖
执行 gradle dependencyInsight 可以查看指定的依赖情况.