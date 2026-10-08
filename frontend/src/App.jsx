import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';

export default function App() {
  const [messages, setMessages] = useState([
    {
      sender: 'ai',
      text: 'Hello! I am your Guanxi (GXI) assistant. Ask me to check token balances or execute token transfers on the blockchain.'
    }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, loading]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!input.trim() || loading) return;

    const userText = input.trim();
    setMessages((prev) => [...prev, { sender: 'user', text: userText }]);
    setInput('');
    setLoading(true);

   try {
      const res = await axios.post('/api/chat', { message: userText });
      console.log('Backend response:', res.data);

      const replyText = res.data?.response?.trim() 
        ? res.data.response 
        : 'Action completed, but no text response was returned.';

      setMessages((prev) => [...prev, { sender: 'ai', text: replyText }]);
    } catch (err) {
      console.error('Chat error:', err);
      setMessages((prev) => [
        ...prev,
        {
          sender: 'ai',
          text: 'Error connecting to backend: ' + (err.response?.data?.message || err.message)
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 700, margin: '40px auto', fontFamily: 'system-ui, sans-serif' }}>
      <header style={{ marginBottom: 20, textAlign: 'center' }}>
        <h1 style={{ margin: 0, color: '#0f172a' }}>Guanxi (GXI) Assistant</h1>
        <p style={{ color: '#64748b', marginTop: 4 }}>Conversational Web3 Token Interface</p>
      </header>

      {/* Chat Window */}
      <div style={{
        border: '1px solid #e2e8f0',
        borderRadius: 12,
        padding: 20,
        height: 480,
        overflowY: 'auto',
        background: '#f8fafc',
        display: 'flex',
        flexDirection: 'column',
        gap: 12
      }}>
        {messages.map((m, idx) => (
          <div
            key={idx}
            style={{
              alignSelf: m.sender === 'user' ? 'flex-end' : 'flex-start',
              maxWidth: '80%'
            }}
          >
            <div style={{
              padding: '10px 14px',
              borderRadius: 10,
              fontSize: 14,
              lineHeight: 1.5,
              background: m.sender === 'user' ? '#2563eb' : '#ffffff',
              color: m.sender === 'user' ? '#ffffff' : '#1e293b',
              boxShadow: '0 1px 2px rgba(0,0,0,0.05)',
              wordBreak: 'break-word'
            }}>
              {m.text}
            </div>
          </div>
        ))}
        {loading && (
          <div style={{ alignSelf: 'flex-start', color: '#64748b', fontSize: 13, fontStyle: 'italic' }}>
            Claude is querying the blockchain...
          </div>
        )}
        {/* Invisible anchor to scroll into view */}
        <div ref={messagesEndRef} />
      </div>

      {/* Input Form */}
      <form onSubmit={sendMessage} style={{ display: 'flex', marginTop: 14, gap: 10 }}>
        <input
          style={{
            flex: 1,
            padding: '12px 16px',
            borderRadius: 8,
            border: '1px solid #cbd5e1',
            fontSize: 14,
            outline: 'none'
          }}
          value={input}
          placeholder="e.g., What is the GXI balance of 0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266?"
          onChange={(e) => setInput(e.target.value)}
        />
        <button
          type="submit"
          disabled={loading}
          style={{
            padding: '12px 22px',
            background: loading ? '#94a3b8' : '#2563eb',
            color: '#fff',
            border: 'none',
            borderRadius: 8,
            fontSize: 14,
            fontWeight: 600,
            cursor: loading ? 'not-allowed' : 'pointer'
          }}
        >
          Send
        </button>
      </form>
    </div>
  );
}