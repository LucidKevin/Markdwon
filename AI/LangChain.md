# LangChain

## 简介

Langchain 是一个开源的Python AI 应用开发框架，它提供了构建基于大模型的 AI 应用所需的模块和工具。通过LangChain，开发者可以轻松地与大型语言模型（LLM）集成，完成文本生成、问答、翻译、对话等任务。LangChain 降低了AI 应用的开发门槛，让任何人都可以基于LLM构建属于自己的创意应用

### **LangChain 特性**

- **LLM 和 提示（Prompt）**：LangChain 对所有LLM 大模型进行了 API抽象，统一了大模型访问 API，同时提供了 Prompt提示模板管理机制
- **链（Chain）**：LangChain 对一些常见的场景封装了一些现成的模块，例如：基于上下文信息的问答系统，自然语言生成SQL 查询等等。因为实现这些任务的过程就像工作流一样，一步一步执行的，所以叫做链（chain）
- **LCEL（LangChain Expression Lauguge**）：LangChain 新版本的核心特性，用于解决工作流编排问题，通过 LCEL 表达式，可以灵活自定义 AI 任务处理流程，也就是灵活自定义链（Chain）
- **检索增强生成（RAG）**：由于大模型（LLM）不了解新的信息，无法回答新的问题，所以我们可以将新的信息导入到 LLM，用于增强LLM生成新内容的质量，这种模式叫做 RAG 模式
- **智能体 （Agents）：**是一种基于大模型（LLM）的应用设计模式，利用 LLM 的自然语言理解和推理能力（LLM作为大脑），根据用户需求自动调用外部系统的，设备共同去完成任务，例如：用户输入"明天请假"，大模型（LLM）自动调用请求系统，发起一个请假申请
- **模型记忆（memory）**：让大模型记住之前的对话内容



### LangChain 架构

<img src="./assets/image-20250721163846126.png" alt=" " style="zoom:50%;" />

**LangChain 框架**由几个部分组成，包括：

- LangChain库：Python 和 JavaScript库。包含接口和集成多种组件的运行时基础，以及现成的链和代理的实现
- LangChain 模板：LangChain 官方提供的一些 AI 任务模板
- LangServe：基于FastAPI 可以将LangChain定义的链（Chain）发布成为Rest API
- LangSmith：开发平台，是一个云服务，支持 LangChain debug、任务监控

**LangChain 库（Libraries）**

- langchain-core：基础抽象和 Langchain 表达语言
- langchain-community：第三方集成，主要包括Langchain 集成的第三方组件
- langchain：主要包括链（Chain）、代理（agent）和检索策略

**LangChain 任务处理流程**

![image-20250721165629282](./assets/image-20250721165629282.png)

如上图，LangChain 提供一套提示词模板（Prompt Template）管理工具，负责处理提示词，然后传递给大模型处理，最后处理大模型返回的结果

LangChain 对大模型的封装主要包含 LLM 和 Chat Model 两种类型

- LLM - 问答模型，模型接收一个文本输入，然后返回一个文本结果
- Chat Model - 对话模型，接收一组对话信息，然后返回对话消息，类似聊天信息一样



### LangChain 核心概念

1. LLMs

LangChain 封装的基础模型，模型接收一个文本输入，然后返回一个文本结果

2. Chat Models

聊天模型的（或者成为对话模型），与 LLMs 不同，这些模型专为对话场景而设计。模型可以接收一组对话消息，然后返回对话消息，类似聊天信息一样

3. 消息（Message）

指的是聊天模型的消息内容，消息类型包括 HumanMessage、AIMessage、SystemMessage、FunctionMessage 和 ToolMessage 等多种类型的消息

4. 提示（Prompts）

 LangChain 封装了一组专门用于提示词（Prompts）管理的工具类，方便我们格式化提示词内容

5. 输出解析器（Output Parsers）

如上图所示，Langchain 接受大模型（LLM）返回的文本内容后，可以使用专门的输出解析器对文本内容进行格式化，例如破解Json、或将 LLM 输出内容转为 python 对象

6. Retrievers

为方便我们将私有数据导入到大模型（LLM），提高模型回答问题质量，LangChain 封装了检索框架，方便我们加载文档数据，切割文档数据，存储文档数据

7. 向量存储（Vector stores）

为支持私有数据的语义相似搜索，LangChain 支持多种向量数据库

8. Agents

智能体（Agents），通常指的是以大模型（LLM）作为决策引擎，根据用户输入的任务，自动调用外部系统、硬件设备共同完成用户的任务，是一种以大模型为核心的应用的设计模型



## 提示词工程

### Prompt templates 介绍

语言模型以文本作为输入 - 这个文本通常被称为提示词（prompt）。在开发过程中，对于提示词通常不能直接硬编码，不利于提示词管理，而是通过提示词模板进行维护，类似开发过程中遇到的短信模板、邮件模板等

![image-20250721181531624](./assets/image-20250721181531624.png)

### Prompt Template

提示词模板本质上跟平时大家使用的邮件模板、短信模板没什么区别，就是一个字符串模板，模板可以包含一组模板参数，通过模板参数值可以替换模板对应的参数。

一个提示词模板可以包含下面内容：

- 发给大语言模型（LLM）的指令
- 一组问答示例，以提醒 AI 以什么格式返回请求
- 发给语言模型的问题

``` python
from langchain.prompts import PromptTemplate
from langchain_core.prompts import ChatPromptTemplate

# 定义一个提示词模板，包含 adjective 和 content两个模板变量
prompt_template = PromptTemplate.from_template(
    "给我讲一个关于{content}的{adjective}笑话。"
)

# chatTemplate
chatTemplate = ChatPromptTemplate.from_template([
    {"role": "system", "content": "你是一个幽默的讲笑话的机器人"},
    {"role": "user", "content": "{{content}}"},
    {"role": "assistant", "content": "{{adjective}}"},
    {"role": "user", "content": "{{adjective}}"}
])

# 通过模板参数格式化提示 m模板
result = prompt_template.format(content="猫", adjective="短短的")
print(result) 
```

### **Chat Prompt Template**

聊天模型（Chat Model）以聊天消息列表作为输入，这个聊天消息列表的消息内容也可以通过提示词模板进行管理。这些聊天消息与原始字符串不同，因为每个消息都与'角色（role）' 相关联

例如，在 OpenAI 的 Chat completion API中，OpenAI的聊天模型，给不同的聊天消息定义了三种角色类型分别是助手（assistant）、人类（human）或系统（System）角色：

- 助手（Assistant）消息指的是当前消息 AI 回答的内容
- 人类（user）消息指的是你发给 AI 的内容
- 系统（System）消息通常是用来给 AI 身份进行描述

创建聊天消息模板例子

``` python
from langchain_core.prompts import ChatPromptTemplate

# 通过一个消息数组创建聊天消息模板
# 数组每一个元素代表一条消息，每个消息元组，第一个元素代表消息角色（也成为消息类型），第二个元素代表消息内容。
# 消息角色：system代表系统消息、human代表人类消息，ai代表LLM返回的消息内容
# 下面消息定义了2个模板参数name和user_input
chat_template = ChatPromptTemplate.from_messages(
    [
        ("system", "你是一位人工智能助手，你的名字是{name}。"),
        ("human", "你好"),
        ("ai", "我很好，谢谢！"),
        ("human", "{user_input}"),
    ]
)

# 通过模板参数格式化模板内容
messages = chat_template.format_messages(name="Bob", user_input="你的名字叫什么？")
print(messages)
```

另一种消息格式例子

``` python 
from langchain.prompts import HumanMessagePromptTemplate
from langchain_core.messages import SystemMessage
from langchain_core.prompts import ChatPromptTemplate

# 使用langchain定义的SystemMessage、HumanMessagePromptTemplate等工具类定义消息，跟前面的例子类似，下面定义了两条消息
chat_template = ChatPromptTemplate.from_messages(
    [
        SystemMessage(
            content=(
                "你是一个乐于助人的助手，可以润色内容，使其看起来起来更简单易读。"
            )
        ),
        HumanMessagePromptTemplate.from_template("{text}"),
    ]
)

# 使用模板参数格式化模板
messages = chat_template.format_messages(text="我不喜欢吃好吃的东西")
print(messages)
```

### MessagesPlaceHolder

这个提示模板负责在特定位置添加消息列表。 在上面的 ChatPromptTemplate 中，我们看到了如何格式化两条消息，每条消息都是一个字符串。 但是，如果我们希望用户传入一个消息列表，我们将其插入到特定位置，该怎么办？ 这就是您使用 MessagesPlaceholder 的方式。

```python
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.messages import HumanMessage
prompt_template = ChatPromptTemplate.from_messages([
    ("system", "You are a helpful assistant"),
    MessagesPlaceholder("msgs")
])
prompt_template.invoke({"msgs": [HumanMessage(content="hi!")]})
```

这将生成两条消息，第一条是系统消息，第二条是我们传入的 HumanMessage。 如果我们传入了 5 条消息，那么总共会生成 6 条消息（系统消息加上传入的 5 条消息）。 这对于将一系列消息插入到特定位置非常有用。 另一种实现相同效果的替代方法是，不直接使用 `MessagesPlaceholder` 类，而是：

``` python
prompt_template = ChatPromptTemplate.from_messages([
    ("system", "You are a helpful assistant"),
    ("placeholder", "{msgs}") # <-- 这是更改的部分
])
```

### Few-shot prompt templates

提示词中包含交互样本的作用是为了帮助模型更好理解用户的意图，从而更好地回答问题或执行任务。小样本提示模板是指的用一组少量的示例来知指导模型处理新的输入。这些样例可以用来『训练』模型，以便模型能够更好地理解和回答类似问题

**示例**

```python
from langchain.prompts.few_shot import FewShotPromptTemplate
from langchain.prompts.prompt import PromptTemplate


examples = [
  {
    "question": "谁的寿命更长，穆罕默德·阿里还是艾伦·图灵？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：穆罕默德·阿里去世时多大？
中间答案：穆罕默德·阿里去世时74岁。
跟进：艾伦·图灵去世时多大？
中间答案：艾伦·图灵去世时41岁。
所以最终答案是：穆罕默德·阿里
"""
  },
  {
    "question": "craigslist的创始人是什么时候出生的？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：craigslist的创始人是谁？
中间答案：craigslist由Craig Newmark创立。
跟进：Craig Newmark是什么时候出生的？
中间答案：Craig Newmark于1952年12月6日出生。
所以最终答案是：1952年12月6日
"""
  },
  {
    "question": "乔治·华盛顿的祖父母中的母亲是谁？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：乔治·华盛顿的母亲是谁？
中间答案：乔治·华盛顿的母亲是Mary Ball Washington。
跟进：Mary Ball Washington的父亲是谁？
中间答案：Mary Ball Washington的父亲是Joseph Ball。
所以最终答案是：Joseph Ball
"""
  },
  {
    "question": "《大白鲨》和《皇家赌场》的导演都来自同一个国家吗？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：《大白鲨》的导演是谁？
中间答案：《大白鲨》的导演是Steven Spielberg。
跟进：Steven Spielberg来自哪里？
中间答案：美国。
跟进：《皇家赌场》的导演是谁？
中间答案：《皇家赌场》的导演是Martin Campbell。
跟进：Martin Campbell来自哪里？
中间答案：新西兰。
所以最终答案是：不是
"""
  }
]



example_prompt = PromptTemplate(input_variables=["question", "answer"], template="问题：{question}\\n{answer}")

# 提取examples示例集合的一个示例的内容，用于格式化模板内容
print(example_prompt.format(**examples[0]))

# 接收examples示例数组参数，通过example_prompt提示词模板批量渲染示例内容
# suffix和input_variables参数用于在提示词模板最后追加内容， input_variables用于定义suffix中包含的模板参数
prompt = FewShotPromptTemplate(
    examples=examples,
    example_prompt=example_prompt,
    suffix="问题：{input}",
    input_variables=["input"]
)

print(prompt.format(input="乔治·华盛顿的父亲是谁？"))
```

### ExampleSelector

这里重用前一部分中的示例集和提示词模板(prompt template)。但是，不会将示例直接提供给`FewShotPromptTemplate`对象，把全部示例插入到提示词中，而是将它们提供给一个`ExampleSelector`对象，插入部分示例。

这里我们使用`SemanticSimilarityExampleSelector`类。该类根据与输入的相似性选择小样本示例。它使用嵌入模型计算输入和小样本示例之间的相似性，然后使用向量数据库执行相似搜索，获取跟输入相似的示例。

- 提示：这里涉及向量计算、向量数据库，在AI领域这两个主要用于数据相似度搜索，例如：查询相似文章内容、相似的图片、视频等等，这里先简单了解下就行。

``` python
from langchain.prompts.example_selector import SemanticSimilarityExampleSelector
from langchain_community.vectorstores import Chroma
from langchain_openai import OpenAIEmbeddings
from langchain_huggingface import HuggingFaceEmbeddings

# 选择开源嵌入模型（如all-MiniLM-L6-v2）
embeddings = HuggingFaceEmbeddings(
    model_name="sentence-transformers/all-MiniLM-L6-v2",
    model_kwargs={"device": "cpu"}  # 使用CPU，如需GPU可改为"cuda"
)


examples = [
  {
    "question": "谁的寿命更长，穆罕默德·阿里还是艾伦·图灵？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：穆罕默德·阿里去世时多大？
中间答案：穆罕默德·阿里去世时74岁。
跟进：艾伦·图灵去世时多大？
中间答案：艾伦·图灵去世时41岁。
所以最终答案是：穆罕默德·阿里
"""
  },
  {
    "question": "craigslist的创始人是什么时候出生的？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：craigslist的创始人是谁？
中间答案：craigslist由Craig Newmark创立。
跟进：Craig Newmark是什么时候出生的？
中间答案：Craig Newmark于1952年12月6日出生。
所以最终答案是：1952年12月6日
"""
  },
  {
    "question": "乔治·华盛顿的祖父母中的母亲是谁？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：乔治·华盛顿的母亲是谁？
中间答案：乔治·华盛顿的母亲是Mary Ball Washington。
跟进：Mary Ball Washington的父亲是谁？
中间答案：Mary Ball Washington的父亲是Joseph Ball。
所以最终答案是：Joseph Ball
"""
  },
  {
    "question": "《大白鲨》和《皇家赌场》的导演都来自同一个国家吗？",
    "answer":
"""
这里需要跟进问题吗：是的。
跟进：《大白鲨》的导演是谁？
中间答案：《大白鲨》的导演是Steven Spielberg。
跟进：Steven Spielberg来自哪里？
中间答案：美国。
跟进：《皇家赌场》的导演是谁？
中间答案：《皇家赌场》的导演是Martin Campbell。
跟进：Martin Campbell来自哪里？
中间答案：新西兰。
所以最终答案是：不是
"""
  }
]


example_selector = SemanticSimilarityExampleSelector.from_examples(
    # 这是可供选择的示例列表。
    examples,
    # 这是用于生成嵌入的嵌入类，该嵌入用于衡量语义相似性。
    embeddings,
    # 这是用于存储嵌入和执行相似性搜索的VectorStore类。
    Chroma,
    # 这是要生成的示例数。
    k=1
)

# 选择与输入最相似的示例。
question = "乔治·华盛顿的父亲是谁？"
selected_examples = example_selector.select_examples({"question": question})
print(f"最相似的示例：{question}")
for example in selected_examples:
    print("\\n")
    for k, v in example.items():
        print(f"{k}：{v}")
```

### 测试提示词

**提示词内容**

```
("system", "你是一个专业的数仓开发师，请根据用户需求修改内容"),("user", "{input}")
```

**返回值**

````sql
以下是对您提供的 SQL 查询的优化建议：

## 主要优化点

1. **简化 DISTINCT 操作**：减少不必要的 DISTINCT 使用
2. **优化 CASE WHEN 表达式**：简化复杂的 CASE 语句
3. **改进 JOIN 操作**：优化子查询和连接条件
4. **日期处理优化**：统一日期比较方式
5. **索引建议**：添加查询提示

## 优化后的 SQL

```sql
SELECT
    c.leibie,
    c.fproject_type_name,
    COUNT(DISTINCT c.fuid) AS cnt, -- 当月分配量
    COUNT(DISTINCT CASE WHEN c.Fcall_min_pro IS NOT NULL AND DATE(c.Fcall_min_pro) BETWEEN '2025-04-01' AND '2025-04-10' THEN c.fuid END) AS call_user_cnt, -- 外呼用户数
    COUNT(DISTINCT CASE WHEN c.Fcall_connect_min_pro IS NOT NULL AND DATE(c.Fcall_connect_min_pro) BETWEEN '2025-04-01' AND '2025-04-10' THEN c.fuid END) AS connect_user_cnt, -- 接通用户数
    COUNT(DISTINCT CASE WHEN c.Fcall_connect_min_pro IS NOT NULL AND DATE(c.Fcall_valid_min_pro) BETWEEN '2025-04-01' AND '2025-04-10' THEN c.fuid END) AS valid_connect_user_cnt, -- 有效接通用户数
    COUNT(DISTINCT CASE WHEN c.Forder_id IS NOT NULL AND c.fis_valid_connect_order = 1 AND DATE(c.forder_create_time) BETWEEN '2025-04-01' AND '2025-04-10' THEN c.fuid END) AS order_succ_user_cnt, -- 下单成功用户数
    COUNT(DISTINCT CASE WHEN c.Floan_order_succ_flag = 1 AND c.fis_valid_connect_order = 1 AND DATE(c.forder_360_time) BETWEEN '2025-04-01' AND '2025-04-10' THEN c.fuid END) AS loan_succ_user_cnt, -- 放款成功用户数
    SUM(CASE WHEN c.Floan_order_succ_flag = 1 AND c.fis_valid_connect_order = 1 AND DATE(c.forder_360_time) BETWEEN '2025-04-01' AND '2025-04-10' AND c.Fbusiness_own = '借钱' THEN c.Floan_amount ELSE 0 END) AS loan_succ_Floan_amount, -- 放款成功金额
    SUM(d.Fcall_valid_pro) AS sc
FROM (
    SELECT
        fproject_type_name,
        CASE 
            WHEN Ftemplate_id IN ("PHJR250403ST162075") THEN "13+在贷"
            WHEN Ftemplate_id IN ("PHJR250401ST161622","PHJR250401ST161624","PHJR250401ST161628","PHJR250401ST161626","PHJR250408ST162547") THEN "新转老"
            WHEN Ftemplate_id IN ("PHJR250407ST162363") THEN "次新"
            WHEN Ftemplate_id IN ("PHJR250403ST162056","PHJR250403ST162059","PHJR250403ST162053","PHJR250403ST162050","PHJR250410ST162723","PHJR250410ST162729","PHJR250410ST162726","PHJR250410ST162732") THEN "降价测试"
            WHEN Ftemplate_id IN ("PHJR250401ST161668") THEN "潜客专项"
            WHEN Ftemplate_id IN ("PHJR250403ST162068","PHJR250403ST162071","PHJR250403ST162065","PHJR250403ST162062") THEN "潜客降价测试"
            WHEN Ftemplate_id IN ("PHJR250401ST161613","PHJR241218ST141770","PHJR241218ST141770","PHJR250402ST161683") THEN "常规名单"
            ELSE NULL
        END AS leibie,
        fuid,
        Fcall_min_pro,
        Fcall_connect_min_pro,
        Fcall_valid_min_pro,
        Forder_id,
        fis_valid_connect_order,
        forder_create_time,
        Floan_order_succ_flag,
        forder_360_time,
        Floan_amount,
        Fbusiness_own
    FROM dp_kf_mart.dm_kf_tel_whole_path_order_detail_df_v2
    WHERE f_p_date BETWEEN '2025-04-01' AND '2025-04-10'
      AND Fbusiness_type_name = '普惠'
      AND fseat_type_md = 'BPO'
) c
LEFT JOIN (
    SELECT 
        fuid,
        COUNT(DISTINCT CASE WHEN Fis_valid_call = '1' AND Fcall_duration >= 30 THEN Fcall_id END) AS Fcall_valid_pro
    FROM dp_kf_mart.dm_dx_call_detail_day
    WHERE Fseat_type <> '微信'
      AND Fis_valid_call = '1'
      AND Fdis_group LIKE '%普惠%'
      AND DATE(Fcall_created_time) BETWEEN '2025-04-01' AND '2025-04-10'
    GROUP BY fuid
) d ON c.fuid = d.fuid
WHERE c.leibie IS NOT NULL
GROUP BY c.leibie, c.fproject_type_name
ORDER BY c.leibie, c.fproject_type_name
LIMIT 100;
```

## 优化说明

1. **简化 CASE WHEN 表达式**：
   - 移除了空字符串的条件分支（这些分支不会匹配任何内容）
   - 为未匹配的情况添加了 ELSE NULL 分支
   - 添加了 WHERE c.leibie IS NOT NULL 过滤掉未分类的记录

2. **统一 COUNT 表达式风格**：
   - 将所有 COUNT(DISTINCT IF(...)) 改为更标准的 COUNT(DISTINCT CASE WHEN ... THEN ... END) 形式

3. **JOIN 优化**：
   - 确保子查询 d 已经按 fuid 分组，减少 JOIN 时的数据处理量

4. **性能建议**：
   - 确保 f_p_date、Fbusiness_type_name、fseat_type_md 等过滤字段有索引
   - 考虑为 Ftemplate_id 创建索引以加速 CASE WHEN 判断
   - 对于大表连接，确保 fuid 字段有索引

5. **其他改进**：
   - 移除了外层查询的 DISTINCT（GROUP BY 已经确保了唯一性）
   - 统一了日期比较的格式
````

**提示词内容**

``` 
("system", "你是一个专业的大数据架构师，请根据用户需求修改内容"),("user", "{input}")
```

**返回内容**

````sql
# SQL 优化建议

以下是对您提供的 SQL 查询的优化建议：

## 主要优化点

1. **减少 DISTINCT 使用**：原查询中多处使用了 DISTINCT，这会增加查询负担
2. **简化 CASE WHEN 表达式**：将复杂的 CASE WHEN 结构优化为更简洁的形式
3. **优化 JOIN 操作**：改进 JOIN 条件和子查询
4. **日期过滤优化**：统一日期过滤条件

## 优化后的 SQL

```sql
SELECT
    c.leibie,
    c.fproject_type_name,
    COUNT(DISTINCT c.fuid) AS cnt, -- 当月分配量
    COUNT(DISTINCT CASE WHEN c.Fcall_min_pro IS NOT NULL AND c.Fcall_min_pro >= '2025-04-01' AND c.Fcall_min_pro < '2025-04-11' THEN c.fuid END) AS call_user_cnt, -- 外呼用户数
    COUNT(DISTINCT CASE WHEN c.Fcall_connect_min_pro IS NOT NULL AND c.Fcall_connect_min_pro >= '2025-04-01' AND c.Fcall_connect_min_pro < '2025-04-11' THEN c.fuid END) AS connect_user_cnt, -- 接通用户数
    COUNT(DISTINCT CASE WHEN c.Fcall_connect_min_pro IS NOT NULL AND c.Fcall_valid_min_pro >= '2025-04-01' AND c.Fcall_valid_min_pro < '2025-04-11' THEN c.fuid END) AS valid_connect_user_cnt, -- 有效接通用户数
    COUNT(DISTINCT CASE WHEN c.Forder_id IS NOT NULL AND c.fis_valid_connect_order = 1 AND c.forder_create_time >= '2025-04-01' AND c.forder_create_time < '2025-04-11' THEN c.fuid END) AS order_succ_user_cnt, -- 下单成功用户数
    COUNT(DISTINCT CASE WHEN c.Floan_order_succ_flag = 1 AND c.fis_valid_connect_order = 1 AND c.forder_360_time >= '2025-04-01' AND c.forder_360_time < '2025-04-11' THEN c.fuid END) AS loan_succ_user_cnt, -- 放款成功用户数
    SUM(CASE WHEN c.Floan_order_succ_flag = 1 AND c.fis_valid_connect_order = 1 AND c.forder_360_time >= '2025-04-01' AND c.forder_360_time < '2025-04-11' AND c.Fbusiness_own = '借钱' THEN c.Floan_amount ELSE 0 END) AS loan_succ_Floan_amount, -- 放款成功金额
    SUM(d.Fcall_valid_pro) AS sc
FROM (
    SELECT
        fproject_type_name,
        CASE 
            WHEN Ftemplate_id IN ("PHJR250403ST162075") THEN "13+在贷"
            WHEN Ftemplate_id IN ("PHJR250401ST161622","PHJR250401ST161624","PHJR250401ST161628","PHJR250401ST161626","PHJR250408ST162547") THEN "新转老"
            WHEN Ftemplate_id IN ("PHJR250407ST162363") THEN "次新"
            WHEN Ftemplate_id IN ("PHJR250403ST162056","PHJR250403ST162059","PHJR250403ST162053","PHJR250403ST162050","PHJR250410ST162723","PHJR250410ST162729","PHJR250410ST162726","PHJR250410ST162732") THEN "降价测试"
            WHEN Ftemplate_id IN ("PHJR250401ST161668") THEN "潜客专项"
            WHEN Ftemplate_id IN ("PHJR250403ST162068","PHJR250403ST162071","PHJR250403ST162065","PHJR250403ST162062") THEN "潜客降价测试"
            WHEN Ftemplate_id IN ("PHJR250401ST161613","PHJR241218ST141770","PHJR241218ST141770","PHJR250402ST161683") THEN "常规名单"
            ELSE NULL
        END AS leibie,
        fuid,
        Fcall_min_pro,
        Fcall_connect_min_pro,
        Fcall_valid_min_pro,
        Forder_id,
        fis_valid_connect_order,
        forder_create_time,
        Floan_order_succ_flag,
        forder_360_time,
        Floan_amount,
        Fbusiness_own
    FROM dp_kf_mart.dm_kf_tel_whole_path_order_detail_df_v2
    WHERE f_p_date BETWEEN '2025-04-01' AND '2025-04-10'
    AND Fbusiness_type_name = '普惠'
    AND fseat_type_md = 'BPO'
) c
LEFT JOIN (
    SELECT 
        fuid,
        COUNT(DISTINCT CASE WHEN Fis_valid_call = '1' AND Fcall_duration >= 30 THEN Fcall_id END) AS Fcall_valid_pro
    FROM dp_kf_mart.dm_dx_call_detail_day
    WHERE Fseat_type <> '微信'
    AND Fis_valid_call = '1'
    AND Fdis_group LIKE '%普惠%'
    AND Fcall_created_time >= '2025-04-01' 
    AND Fcall_created_time < '2025-04-11'
    GROUP BY fuid
) d ON c.fuid = d.fuid
WHERE c.leibie IS NOT NULL
GROUP BY c.leibie, c.fproject_type_name
ORDER BY c.leibie, c.fproject_type_name
LIMIT 100
```

## 优化说明

1. **日期比较优化**：
   - 使用 `>=` 和 `<` 代替 `BETWEEN` 和 `date()` 函数，避免函数调用
   - 使用 `'2025-04-11'` 作为上限而不是 `'2025-04-10'` 以确保包含4月10日全天数据

2. **CASE WHEN 简化**：
   - 移除了空的条件分支（如 `when Ftemplate_id in("") then "次新降价测试"`）
   - 添加了 ELSE NULL 以确保所有记录都有明确的分类

3. **JOIN 优化**：
   - 在子查询 d 中预先聚合数据，减少 JOIN 时的数据处理量
   - 添加了 `WHERE c.leibie IS NOT NULL` 过滤掉未分类的记录

4. **DISTINCT 优化**：
   - 保留了必要的 DISTINCT 操作，但减少了不必要的使用

5. **其他改进**：
   - 统一了 CASE WHEN 和 IF 的写法，使用更一致的 CASE WHEN 语法
   - 移除了原查询中重复的模板ID（如 "PHJR241218ST141770" 出现两次）

这些优化应该能显著提高查询性能，特别是在处理大量数据时。
````

## LCEL（LangChain Expression Language）

LCEL（LangChain Expression Language）是 LangChain 生态中用于编排复杂 AI 工作流的核心工具，其设计初衷是解决传统链式调用（Chain）在灵活性、可靠性和生产级适配性上的不足。它通过 “表达式语法” 将不同组件（如 LLM、检索器、工具、解析器等）串联成可复用的工作流，并内置了一系列生产级特性，让开发者能从快速原型直接过渡到规模化部署，无需重构代码。

### LCEL 介绍

#### 一、核心设计理念

LCEL 的本质是一种 “声明式” 的工作流描述语言，通过简单的操作符（如`|`表示 “传递”，`&`表示 “并行”）将组件组合成链。例如：

```python
# 一个简单的RAG链：检索器 | 提示模板 | LLM | 解析器
rag_chain = retriever | prompt | llm | parser
```

这种语法的优势在于：

- 组件间解耦：每个组件（检索器、LLM 等）可独立替换（如把 OpenAI 换成 DeepSeek，只需改`llm`实例）；
- 逻辑直观：工作流的执行顺序与代码书写顺序一致，便于调试和维护；
- 可扩展性：支持嵌套组合（如在并行分支中再嵌套子链），轻松构建数百步的复杂流程。

#### 二、核心特性深度解析

1. **一流的流式支持（Streaming）**

流式是提升用户体验的关键特性，尤其在对话、长文本生成场景中。LCEL 的流式支持做到了 “端到端无阻塞”：

- **实时增量输出**：LLM 生成第一个 token（字符块）时，会立即通过流式接口返回，无需等待完整响应。例如，调用 GPT 生成一篇文章时，用户会像 “打字” 一样逐步看到内容，而非等待 10 秒后一次性加载；
- **解析器与流式协同**：即使中间有解析器（如将 LLM 输出转为 JSON），LCEL 也能流式传递 “部分解析结果”。例如，LLM 生成`{"name": "Alic`时，解析器可先返回`{"name": "Alic"}`的中间状态，避免用户等待；
- **适用场景**：聊天机器人、实时报告生成、代码实时生成等需要 “低延迟反馈” 的场景。

2. **同步与异步无缝切换**

LCEL 的链天生支持同步（`invoke`）和异步（`ainvoke`）两种调用方式，且共享同一套代码：

- 同步调用：适合快速原型（如 Jupyter 笔记本），代码简洁：

  ```python
  result = rag_chain.invoke({"query": "什么是LCEL？"})  # 同步等待结果
  ```

- 异步调用：适合高并发场景（如 LangServe 服务器），可同时处理数千个请求而不阻塞：

  ```python
  result = await rag_chain.ainvoke({"query": "什么是LCEL？"})  # 异步非阻塞
  ```

- **核心价值**：开发者无需为 “原型” 和 “生产” 写两套代码，一套逻辑既能在笔记本中快速验证，也能在服务器中支撑高并发。

**3. 自动优化的并行执行**

在复杂工作流中，多个可独立执行的步骤（如从多个检索源获取文档、调用多个工具）会被 LCEL 自动并行处理，大幅降低整体延迟。

例如，一个需要从 3 个向量库检索文档的 RAG 链：

```python
# 3个独立的检索器（可并行）
retrievers = [retriever1, retriever2, retriever3]
# 并行执行所有检索，再合并结果
parallel_retrieve = lambda x: [r.invoke(x) for r in retrievers] | merge_docs
# 整体链：并行检索 → 生成提示 → LLM生成
chain = parallel_retrieve | prompt | llm
```

LCEL 会自动将 3 个检索器的调用并行化（而非串行等待），原本需要 3 秒（每个 1 秒）的步骤，现在 1 秒即可完成。

**4. 重试与回退：提升可靠性的 “安全网”**

在生产环境中，API 调用失败（如 LLM 超时、检索器无响应）是常见问题。LCEL 允许为链的任意步骤配置重试策略或备用方案：

- 重试机制：对临时故障（如网络波动）自动重试，可指定重试次数、间隔和触发条件（如仅重试5xx错误）：

  ```python
  from langchain_core.runnables import RunnableRetry
  
  # 对LLM步骤配置：最多重试3次，间隔1秒
  llm_with_retry = RunnableRetry(llm, max_attempts=3, delay=1)
  chain = prompt | llm_with_retry
  ```

- 回退机制：当主步骤失败时，自动切换到备用组件（如主 LLM 超时后用轻量模型兜底）：

  ```python
  from langchain_core.runnables import RunnableFallbacks
  
  # 主模型失败时，用备用模型回退
  llm_with_fallback = RunnableFallbacks(llm, fallbacks=[lightweight_llm])
  chain = prompt | llm_with_fallback
  ```

- **价值**：将链的故障率从 10% 降至 0.1%，尤其适合需要高可用性的金融、医疗等场景。

**5. 中间结果访问：透明化与可调试**

复杂链的中间步骤（如检索到的文档、LLM 的原始输出）往往对调试或用户体验至关重要。LCEL 支持实时访问和流式传输中间结果：

- 调试场景：通过invoke 的 return_intermediate_steps=True 参数，获取每个步骤的输出：

  ```python
  result = chain.invoke({"query": "xxx"}, return_intermediate_steps=True)
  # 查看检索到的文档、提示模板内容等中间结果
  print(result["intermediate_steps"])
  ```

- 用户体验场景：流式传输中间结果，让用户感知进度（如 “正在检索相关文档...→已获取 3 篇文档→正在生成回答...”）：

  ```python
  for chunk in chain.stream({"query": "xxx"}):
      if "intermediate" in chunk:
          print(f"进度：{chunk['intermediate']}")  # 实时展示中间状态
      else:
          print(chunk["output"], end="")  # 最终输出
  ```

**6. 输入输出模式：标准化与验证**

LCEL 会自动从链的结构中推断输入和输出的 “数据模式”（基于 Pydantic 和 JSONSchema），实现：

- **输入验证**：确保传入链的参数符合预期（如必填字段、格式约束），避免无效请求进入流程；
- **输出标准化**：强制输出符合预设格式（如 JSON 结构），便于下游系统解析；
- **LangServe 集成**：模式会被自动暴露给 LangServe 服务器，生成 OpenAPI 文档，方便前端或其他服务调用时参考。

例如，一个需要`query`和`user_id`输入的链，其自动生成的模式可能为：

json

```json
{
  "type": "object",
  "properties": {
    "query": {"type": "string"},
    "user_id": {"type": "string"}
  },
  "required": ["query"]
}
```

#### 三、总结：LCEL 的核心价值

LCEL 不是简单的 “链式调用语法”，而是一套为 “生产级 AI 工作流” 设计的完整解决方案。它通过：

- 简化复杂流程的编排（表达式语法）；
- 内置生产级特性（流式、异步、并行、重试）；
- 打通 “原型 - 生产” 的鸿沟（一套代码适配全场景）；

让开发者能更高效地构建可靠、可扩展的 AI 应用，从简单的 “LLM 调用” 升级到复杂的 “多工具协同、多步骤推理” 系统。



### Runnable Interface

Runnable 接口是 LangChain 中实现组件标准化和链式调用的核心协议，它定义了一套统一的方法让不同组件（如 LLM、提示模板、解析器等）可以无缝协作。通过实现这个接口，各种组件可以被视为 “可运行对象”，并以标准方式组合、调用和扩展。

#### 一、Runnable 接口的核心方法

Runnable 接口定义了一系列同步和异步方法，用于处理输入、执行逻辑并返回结果：

**1. 核心执行方法**

- **`invoke(input)`**
  同步执行链，处理单个输入并返回最终输出。

  ```python
  result = llm.invoke("请解释量子计算")  # 同步调用LLM
  ```

- **`batch(inputs, config=None)`**
  批量处理多个输入，支持并发（通过 `config={"max_concurrency": 5}`）。

  ```python
  results = llm.batch(["问题1", "问题2", "问题3"], config={"max_concurrency": 3})
  ```

- **`stream(input)`**
  流式返回结果，适用于长文本生成（如聊天机器人）。返回一个生成器，每次产生一个输出块。

  ```python
  for chunk in llm.stream("请写一篇关于AI的文章"):
      print(chunk, end="", flush=True)  # 实时打印每个生成的字符
  ```

**2. 异步方法（用于高并发场景）**

- **`ainvoke(input)`**
  异步执行链，需在 `async` 函数中使用 `await`。

  ```python
  async def process():
      result = await llm.ainvoke("请解释区块链")
  ```

- **`abatch(inputs, config=None)`**
  异步批量处理，高效处理大量请求。

  ```python
  async def batch_process():
      results = await llm.abatch(["问题1", "问题2"], config={"max_concurrency": 10})
  ```

- **`astream(input)`**
  异步流式返回结果，适合实时交互场景。

  ```python
  async def stream_response():
      async for chunk in llm.astream("请讲一个故事"):
          print(chunk, end="", flush=True)
  ```

**3. 高级调试与监控方法**

- **`astream_log(input)`**
  异步流式返回中间步骤和最终结果，用于调试复杂链。

  ```python
  async for log in chain.astream_log({"query": "什么是LCEL"}):
      print(f"步骤: {log.step_name}, 输出: {log.output}")
  ```

- **`astream_events(input)`**（Beta）
  异步流式返回链执行过程中的事件（如工具调用开始 / 结束），用于监控和可视化。

  ```python
  async for event in chain.astream_events({"input": "计算1+1"}):
      print(f"事件: {event.name}, 时间: {event.timestamp}")
  ```

#### 二、组件的输入输出类型

不同组件实现 Runnable 接口时，有特定的输入输出类型：

| 组件类型       | 输入类型                           | 输出类型               |
| -------------- | ---------------------------------- | ---------------------- |
| **提示模板**   | `dict`（如 `{"question": "xxx"}`） | 格式化后的提示字符串   |
| **LLM**        | `str` 或 `ChatMessage` 列表        | `str`（文本响应）      |
| **聊天模型**   | `str` 或 `ChatMessage` 列表        | `ChatMessage` 对象     |
| **输出解析器** | LLM/ChatModel 的输出               | 解析后的结构化数据     |
| **检索器**     | `str`（查询文本）                  | `Document` 列表        |
| **工具**       | `str` 或 `dict`                    | 工具执行结果（自定义） |

####  三、输入输出模式（Schema）

Runnable 接口提供自动生成的 Pydantic 模型，用于验证输入输出格式：

**1. `input_schema`**

获取链的输入格式定义，可用于请求验证：

```python
print(chain.input_schema)
# 输出示例:
# class Input(pydantic.BaseModel):
#     query: str
#     history: Optional[List[ChatMessage]] = []
```

**2. `output_schema`**

获取链的输出格式定义，可用于结果解析：

```python
print(chain.output_schema)
# 输出示例:
# class Output(pydantic.BaseModel):
#     answer: str
#     sources: List[str]
```

####  四、流式处理详解

流式是 Runnable 接口的核心优势，适用于实时交互场景：

**1. 同步流式 (`stream`)**

```python
from langchain.chat_models import ChatOpenAI

llm = ChatOpenAI(streaming=True)
for chunk in llm.stream("讲一个笑话"):
    print(chunk.content, end="", flush=True)

# 输出示例（实时显示）:
# 为什么程序员总把万圣节和圣诞节搞混？因为 Oct 31 == Dec 25!
```

**2. 异步流式 (`astream`)**

```python
async def stream_answer():
    async for chunk in llm.astream("解释量子计算"):
        print(chunk.content, end="", flush=True)

# 在async函数中调用
await stream_answer()
```

**3. 带中间步骤的流式 (`astream_log`)**

```python
async def stream_logs():
    async for log in chain.astream_log({"input": "什么是AI"}):
        if log.step_name == "检索文档":
            print(f"已检索到 {len(log.output)} 篇相关文档")
        elif log.step_name == "生成回答":
            print("正在生成回答:", log.output.content)

await stream_logs()
```

####  **五、自定义 Runnable 对象**

通过实现 Runnable 接口，可以创建自定义组件并无缝集成到 LangChain 中：

```python
from langchain_core.runnables import Runnable

class CustomComponent(Runnable):
    def invoke(self, input):
        # 自定义处理逻辑
        return f"处理后的结果: {input}"
    
    async def ainvoke(self, input):
        # 异步处理逻辑
        return await some_async_function(input)

# 使用自定义组件
component = CustomComponent()
result = component.invoke("原始输入")
```

####  **六、Runnable 的组合与扩展**

Runnable 对象可以通过操作符组合成更复杂的链：

**1. 顺序组合 (`|`)**

```python
chain = retriever | prompt | llm | parser
# 等价于: 检索文档 → 填充提示 → 调用LLM → 解析结果
```

**2. 并行组合 (`&`)**

```python
from langchain_core.runnables import RunnableParallel

parallel_chain = RunnableParallel(
    docs=retriever,
    summary=lambda x: x["query"] | summarizer
)
# 并行执行: 检索文档 + 生成查询摘要
```

**3. 条件分支**

```python
from langchain_core.runnables import RunnableBranch

branch = RunnableBranch(
    (lambda x: "紧急" in x["query"], emergency_handler),
    default_handler
)
# 如果查询包含"紧急"，则走emergency_handler，否则走default_handler
```

####  七、应用场景

Runnable 接口使 LangChain 组件具有极高的灵活性，适用于：

1. **实时聊天机器人**：通过 `stream`/`astream` 实现即时响应
2. **复杂推理系统**：组合多个工具和模型，通过 `astream_log` 监控中间步骤
3. **批处理任务**：使用 `batch`/`abatch` 高效处理大量请求
4. **API 服务**：基于 `input_schema`/`output_schema` 自动生成接口文档
5. **可扩展架构**：自定义组件无缝集成到现有链中

通过实现 Runnable 接口，LangChain 组件获得了一致的调用方式和强大的组合能力，让开发者能更高效地构建从简单到复杂的各类 LLM 应用。

### Chain

几乎所有的 LLM 应用程序都涉及不止一步的操作，而不仅仅是调用语言模型。 让我们使用 `LangChain 表达式语言` (`LCEL`) 构建一个简单的链，该链结合了一个提示、模型和解析器，并验证流式传输是否正常工作。 我们将使用 [StrOutputParser](https://api.python.langchain.com/en/latest/output_parsers/langchain_core.output_parsers.string.StrOutputParser.html) 来解析模型的输出。这是一个简单的解析器，从 `AIMessageChunk` 中提取 `content` 字段，给出模型返回的 `token`。

LCEL 是一种*声明式*的方式，通过将不同的 LangChain 原语链接在一起来指定一个“程序”。使用 LCEL 创建的链可以自动实现 `stream` 和 `astream`，从而实现对最终输出的流式传输。事实上，使用 LCEL 创建的链实现了整个标准 Runnable 接口。

```python
#astream_chain.py
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
prompt = ChatPromptTemplate.from_template("给我讲一个关于{topic}的笑话")
parser = StrOutputParser()
chain = prompt | model | parser
async for chunk in chain.astream({"topic": "鹦鹉"}):
    print(chunk, end="|", flush=True)
    
```

``` 
|一个|人|去|宠|物|店|买|鹦|鹉|。|店|员|说|：“|这|只|鹦|鹉|会|说|话|。”|
|买|回|家|后|，|那|人|发|现|鹦|鹉|只|会|说|一|句|话|：“|我|是|鹦|鹉|。”|
|那|人|就|去|找|店|员|，|说|：“|你|不|是|说|这|只|鹦|鹉|会|说|话|吗|？|它|只|会|说|‘|我|是|鹦|鹉|’|。”|
|店|员|回|答|：“|它|确|实|会|说|话|，|你|想|它|怎|么|可能|知|道|自|己|是|鹦|鹉|呢|？”||
```

请注意，即使我们在上面的链条末尾使用了`parser`，我们仍然可以获得流式输出。`parser`会对每个流式块进行操作。许多LCEL基元也支持这种转换式的流式传递，这在构建应用程序时非常方便。

自定义函数可以被设计为返回生成器，这样就能够操作流。

某些可运行实体，如提示模板和聊天模型，无法处理单个块，而是聚合所有先前的步骤。这些可运行实体可以中断流处理。

LangChain表达语言允许您将链的构建与使用模式（例如同步/异步、批处理/流式等）分开。如果这与您构建的内容无关，您也可以依赖于标准的命令式编程方法，通过在每个组件上调用invoke、batch或stream，将结果分配给变量，然后根据需要在下游使用它们。



## LangServe 服务部署

### 概述

LangServe 帮助开发者将 LangChain 可运行链部署为  REST API

该库集成了FastAPI，并使用 pydantic 进行数据验证

Pydantic 是一个在 Python中 用于数据验证和解析的第三方库，现在是Python 中使用广泛的数据验证库

- 它利用声明式方式定义数据模型和 Python 类型提示的强大功能来执行数据验证和序列化，使得您的代码更加可靠、更加可读、更加简洁且易于调试
- 它还可以从模型生成 json 架构，提供自动生成文档等功能，从而轻松与其他工具集成

此外，它还提供了一个客户端，可用于调用部署在服务器上的可运行对象。JavaScript 客户端可在 LangChain.js 中找到

### 特性

- 从 LangChain 对象自动推断输入和输出模式，并在每次 API 调用中执行，提供丰富的错误信息
- 带有 JSONSchema 和 Swagger 的 API 文档页面（插入示例链接）
- 高效的 `/invoke`、`/batch` 和 `/stream` 端点，支持单个服务器上的多个并发请求
- `/stream_log` 端点，用于流式传输链/代理的所有（或部分）中间步骤
- **新功能** 自 0.0.40 版本起，支持 `/stream_events`，使流式传输更加简便，无需解析 `/stream_log` 的输出。
- 使用经过严格测试的开源 Python 库构建，如 FastAPI、Pydantic、uvloop 和 asyncio。
- 使用客户端 SDK 调用 LangServe 服务器，就像本地运行可运行对象一样（或直接调用 HTTP API）

### 限制

- 目前不支持服务器发起的事件的客户端回调
- 当使用 Pydantic V2 时，将不会生成 OpenAPI 文档。FastAPI 支持[混合使用 pydantic v1 和 v2 命名空间](https://github.com/tiangolo/fastapi/issues/10360)。更多细节请参见下面的章节。

### 安装

**LangChain CLI**

使用 `LangChain CLI` 快速启动 `LangServe` 项目

要使用 langchain CLI，请确保已安装最新版本的 `langchain-cli`。您可以使用 `pip install -U langchain-cli` 进行安装。

**设置**

注意：使用 `poetry` 进行依赖管理的，请参考poetry 文档了解更多信息

**1、使用 langchain cli 命令创建新应用**

``` 
langchain app new langserve
```

**2、在 add_routes 中定义可运行的对象。转到Server.py 进行编辑**

```
add_routes(app. NotImplemented)
```

**3、使用 `poetry` 添加第三方包（例如 langchain-openai、langchain-anthropic、langchain-mistral 等）**

```
#安装pipx，参考：https://pipx.pypa.io/stable/installation/
pip install pipx 
#加入到环境变量，需要重启PyCharm 
pipx ensurepath

# 安装poetry，参考：https://python-poetry.org/docs/
pipx install poetry


#安装 langchain-openai 库，例如：poetry add [package-name]
poetry add langchain
poetry add langchain-openai 
```

**4、设置相关环境变量**

```
export OPENAI_API_KEY="sk-..."
```

**5、启动您的应用**

```
poetry run langchain serve --port=8000
```

