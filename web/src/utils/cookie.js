import Cookies from 'js-cookie'

// cookie存的key值
const TOKENKEY = 'k_____token'

export function setCookie(key, value) {
  return Cookies.set(key, value)
}
export function getCookie(key) {
  return Cookies.get(key)
}

export function removeCookie(key) {
  return Cookies.remove(key)
}

//----------token---------------
export function getToken() {
  return Cookies.get(TOKENKEY)
}

export function setToken(token) {
  return Cookies.set(TOKENKEY, token)
}

export function removeToken() {
  return Cookies.remove(TOKENKEY)
}









