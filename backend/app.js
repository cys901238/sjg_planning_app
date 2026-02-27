require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const db = require('./db');
const app = express();
const port = process.env.PORT || 3000;

app.use(bodyParser.json());

app.get('/api/ping', (req,res)=>{
  res.json({ok:true, time: new Date().toISOString()});
});

app.post('/api/users', async (req,res)=>{
  const userid = (req.body.userid || req.body.name || req.body.user || req.body.username || '').trim();
  const uname = (req.body.uname || '').trim();
  const dept = (req.body.dept || '').trim();
  const pw = (req.body.pw || req.body.password || '').trim();

  if(!userid || !uname || !dept || !pw) {
    return res.status(400).json({error:'userid, uname, dept, pw are required'});
  }

  const hashedPw = crypto.createHash('sha256').update(pw, 'utf8').digest('hex');
  const delFlag = (req.body.del || 'N').trim().charAt(0) || 'N';

  try{
    await db.query(
      'INSERT INTO users (userid, uname, dept, pw, del) VALUES (?, ?, ?, ?, ?)',
      [userid, uname, dept, hashedPw, delFlag]
    );
    return res.json({ok:true, userid});
  }catch(err){
    console.error('db insert error', err);
    return res.status(500).json({error:err.message});
  }
});



app.post('/api/login', async (req, res) => {
  const userid = (req.body.userid || '').trim();
  const pw = (req.body.pw || '').trim();
  if (!userid || !pw) {
    return res.status(400).json({ error: 'userid and pw required' });
  }
  try {
    const [rows] = await db.query('SELECT pw FROM users WHERE userid = ? LIMIT 1', [userid]);
    if (rows.length === 0 || !rows[0].pw) {
      return res.json({ ok: true, match: false });
    }
    const hashedPw = crypto.createHash('sha256').update(pw, 'utf8').digest('hex');
    const match = rows[0].pw === hashedPw;
    return res.json({ ok: true, match });
  } catch (err) {
    console.error('login error', err);
    return res.status(500).json({ error: err.message });
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
