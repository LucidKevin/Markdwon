# Cursor

## 基础环境要求
- VSCode
- JDK (推荐 JDK 8 或更高版本)
- Maven (可选，用于依赖管理)

## VSCode 扩展
**必要扩展**

1. **Extension Pack for Java**

2. **Java Extension Pack**

**推荐扩展**

1. **Java Code Generators**
   
2. **Java Dependency Viewer**
   
3. **Java Test Runner**
   
4. **Spring Boot Extension Pack**

## VSCode 配置
### 基础配置
在 `.vscode/settings.json` 中添加以下配置：
```json
{
    "java.project.sourcePaths": ["."],
    "java.project.outputPath": "out/production/Base",
    "java.project.referencedLibraries": [],
    "java.compile.nullAnalysis.mode": "automatic",
    "java.configuration.updateBuildConfiguration": "automatic"
}
```

### 调试配置
在 `.vscode/launch.json` 中添加以下配置：
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug (Launch)",
            "request": "launch",
            "mainClass": "${file}",
            "projectName": "Base"
        }
    ]
}
```

## 常用快捷键
- `F5`: 启动调试
- `Ctrl + F5`: 运行不调试
- `F10`: 单步跳过
- `F11`: 单步进入
- `Shift + F11`: 单步跳出
- `Ctrl + Space`: 触发建议
- `Ctrl + Shift + Space`: 触发参数提示
- `Alt + Up/Down`: 移动当前行
- `Ctrl + D`: 选择下一个匹配项

## 常见问题解决
### 编译输出路径问题
- 确保 `.vscode/settings.json` 中的 `java.project.outputPath` 设置正确
- 默认应该设置为 `out/production/项目名`

### 代码提示不工作
- 检查 Java 扩展是否正确安装
- 重新加载 VSCode 窗口
- 检查 Java 环境变量是否正确设置

### 调试配置问题
- 确保 `launch.json` 中的 `mainClass` 设置正确
- 检查断点是否正确设置
- 确保项目已经编译

## 最佳实践
1. 使用 Maven 或 Gradle 管理项目依赖
2. 定期清理编译输出目录
3. 使用代码格式化保持代码风格一致
4. 善用代码片段提高开发效率
5. 使用版本控制（如 Git）管理代码

## 推荐设置
1. 开启自动保存
2. 设置合适的字体和主题
3. 配置代码格式化规则
4. 设置适当的缩进大小
5. 开启代码提示和自动补全

## 更新和维护
- 定期更新 VSCode 和 Java 扩展
- 关注 Java 扩展的更新日志
- 及时解决扩展冲突问题