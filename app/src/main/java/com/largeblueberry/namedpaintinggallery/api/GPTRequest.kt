package com.largeblueberry.namedpaintinggallery.api

data class GPTRequest(//gpt한테 보낼 데이터 클래스 정의
    val model: String = "gpt-4",
    val messages: List<Map<String, String>>,
    val temperature: Double = 0.7//gpt 창의성 조절이라는데.. 이값이 1.0이 디폴트. 1.0하면 gpt가 말이 좋아서 창의적이지 바둑이가 됨.
)

data class GPTResponse(
    val choices: List<Choice>
)

data class Choice(//gpt를 써보면 답변이 2개이상으로 나오는 경우가 있는데 이때 선택함. 근데, 이거는 명화 어플에는 불필요. 그냥 [0]으로 설정할거임.
    val message: Map<String, String>
)