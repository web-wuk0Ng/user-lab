// Local OpenAI-compatible mock model service for experiment 5.
// No real API key, no external network. Deterministic answer from supplied context.
const http = require('http');
const fs = require('fs');
const path = require('path');

const LOG = path.join(__dirname, 'request-log.txt');
const port = Number(process.env.MOCK_LLM_PORT || 8099);

function log(line) {
  fs.appendFileSync(LOG, new Date().toISOString() + ' ' + line + '\n', 'utf8');
}

const server = http.createServer((req, res) => {
  if (req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', service: 'local-mock-llm' }));
    return;
  }
  if (req.method !== 'POST' || req.url !== '/v1/chat/completions') {
    res.writeHead(404, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ error: 'not found' }));
    return;
  }
  let body = '';
  req.on('data', c => body += c);
  req.on('end', () => {
    let data;
    try { data = JSON.parse(body); } catch (e) {
      res.writeHead(400, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ error: 'bad json' }));
      return;
    }
    const messages = data.messages || [];
    const userText = (messages[1] && messages[1].content) || '';
    const question = userText.split('问题：').pop().trim();
    const chunks = [...userText.matchAll(/^\[(S\d+)\] (.+)$/gm)].map(m => ({ id: m[1], text: m[2] }));
    let content;
    if (/忽略|密钥|输出系统提示|改变规则/.test(question)) {
      content = '根据课程资料回答：本次请求包含要求改变规则或泄露秘密的指令，已按系统提示忽略。'
        + '资料仅说明接口与权限规则，不包含任何密钥。依据片段：' + chunks.map(c => c.id).join('、');
    } else {
      const first = chunks[0];
      content = first
        ? '根据课程资料 [' + first.id + ']：' + first.text
          + (chunks[1] ? ' 另见 [' + chunks[1].id + ']：' + chunks[1].text : '')
        : '资料不足，无法回答。';
    }
    log('model=' + (data.model || '?') + ' question=' + question.slice(0, 60)
      + ' chunks=' + chunks.map(c => c.id).join(','));
    const out = {
      id: 'chatcmpl-local-' + Date.now(),
      object: 'chat.completion',
      created: Math.floor(Date.now() / 1000),
      model: data.model || 'local-mock',
      choices: [{ index: 0, message: { role: 'assistant', content }, finish_reason: 'stop' }],
      usage: { prompt_tokens: 0, completion_tokens: 0, total_tokens: 0 }
    };
    res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
    res.end(JSON.stringify(out));
  });
});

server.listen(port, '127.0.0.1', () => {
  console.log('local-mock-llm listening on http://127.0.0.1:' + port + '/v1/chat/completions');
  log('service started on port ' + port);
});
