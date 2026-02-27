require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const db = require('./db');
const app = express();
const port = process.env.PORT || 3000;

app.use(bodyParser.json());

app.get('/api/ping', (req,res)=>{
  res.json({ok:true, time: new Date().toISOString()});
});

app.post('/api/users', async (req,res)=>{
  const name = req.body.name || req.body.userid || req.body.user || req.body.username;
  if(!name) return res.status(400).json({error:'name (or userid) is required'});
  try{
    const [result]= await db.query('INSERT INTO users (userid) VALUES (?)', [name]);
    return res.json({ok:true, id: result.insertId});
  }catch(err){
    console.error('db insert error', err);
    return res.status(500).json({error:err.message});
  }
});


// Temporary test endpoint: store users in an in-memory array for testing when DB is unavailable.
const inMemoryUsers = [];
app.post('/api/users-test', (req, res) => {
  const { name } = req.body;
  if (!name) return res.status(400).json({ error: 'name is required' });
  const id = inMemoryUsers.length + 1;
  inMemoryUsers.push({ id, name, created_at: new Date().toISOString() });
  return res.json({ ok: true, id });
});

// Check whether a userid/name exists in users table
app.get('/api/users/exists', async (req, res) => {
  const q = req.query.userid || req.query.name;
  console.log('/api/users/exists called - query=', q, 'from=', req.ip, 'headers=', { host: req.headers.host, referer: req.headers.referer });
  if (!q) return res.status(400).json({ error: 'userid or name query required' });
  try {
    const [rows] = await db.query('SELECT 1 FROM users WHERE userid = ? LIMIT 1', [q]);
    console.log('/api/users/exists result for', q, '=>', rows.length > 0);
    return res.json({ exists: rows.length > 0 });
  } catch (err) {
    console.error('db exists error', err);
    return res.status(500).json({ error: err.message });
  }
});

app.listen(port, "0.0.0.0", ()=>{
  console.log('backend listening on', port);
});
