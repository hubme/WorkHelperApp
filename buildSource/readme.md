## 说明

模块名 `buildSrc` 为 Gradle 保留字。

> A change in buildSrc causes the whole project to become out-of-date.
> Thus, when making small incremental changes, the --no-rebuild command-line option is often helpful to get faster feedback.
> Remember to run a full build regularly or at least when you’re done, though.

buildSrc 依赖更新将重新构建整个项目。

## 什么是 buildSrc

摘自 [Gradle 文档](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html)：当运行 Gradle 时会检查项目中是否存在一个名为 buildSrc 的目录。然后 Gradle 会自动编译并测试这段代码，并将其放入构建脚本的类路径中, 对于多项目构建，只能有一个 buildSrc 目录，该目录必须位于根项目目录中, buildSrc 是 Gradle 项目根目录下的一个目录，它可以包含我们的构建逻辑，与脚本插件相比，buildSrc 应该是首选，因为它更易于维护、重构和测试代码。

