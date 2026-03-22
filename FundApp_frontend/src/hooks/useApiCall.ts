import { useState, useCallback } from 'react';
import api from '../api/Api';

// Generic hook to manage loading and error states for any async function
export const useApiCall = <T>(apiFunction: (...args: any[]) => Promise<T>) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const callApi = useCallback(
    async (...args: any[]) => {
      setIsLoading(true);
      setError(null);
      try {
        const result = await apiFunction(...args);
        return result;
      } catch (err) {
        setError('An error occurred while fetching data');
        throw err;
      } finally {
        setIsLoading(false);
      }
    },
    [apiFunction]
  );

  return { callApi, isLoading, error };
};

// Example usage for specific API calls
export const useAllHistoryData = () => {
  const getHistoryData = async () => {
    return await api.get('/history/getallhistory').then(res => res.data);
  };

  return useApiCall(getHistoryData);
};

export const useGetAllFunds = () => {
  const getFundsData = async () => {
    return await api.get('/history/gethistorydatawithchanges').then(res => res.data);
  };

  return useApiCall(getFundsData);
};

export const useFundById = (fundId: string) => {
  const getFundbyId = async () => {
    return await api.get(`/fund/getfundbyid/${fundId}`).then((res) => res.data);
  };

  return useApiCall(getFundbyId);
};

export const useGetPosts = () => {
  const getPosts = async () => {
    return await api.get('/posts').then(res => res.data);
  };

  return useApiCall(getPosts);
};

export const useTCMBUSDTRY = (startDateS: string, endDateS : string) => {
  const getTCMBUSDTRY = async () => {
    return await api.get(`/tcmb/getusdtrywithdate`, { params: { startDate: startDateS, endDate: endDateS  } }).then((res) => res.data);
  };

  return useApiCall(getTCMBUSDTRY);
};
