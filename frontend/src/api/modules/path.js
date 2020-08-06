import ApiService from '@/api'

const BASE_URL = '/paths'

const PathService = {
  get(sourceId, targetId, type) {
    return ApiService.get(`${BASE_URL}?source=${sourceId}&target=${targetId}&type=${type}`)
  }
}

export default PathService
